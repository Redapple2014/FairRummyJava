package com.skillengine.repository;

import com.skillengine.rummy.message.ScoreUpdate;
import com.skillengine.rummy.message.UserScore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.Data;
import org.jdbi.v3.core.ConnectionFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;
import org.jspecify.annotations.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.Types.ARRAY;
import static java.sql.Types.INTEGER;
import static java.sql.Types.REF_CURSOR;
import static java.sql.Types.TIMESTAMP_WITH_TIMEZONE;
import static java.sql.Types.VARCHAR;
import static java.util.stream.Collectors.groupingBy;
import static org.jdbi.v3.core.transaction.TransactionIsolationLevel.READ_COMMITTED;

public final class GameRepository implements AutoCloseable {

    private final Jdbi jdbi;
    private final HikariDataSource dataSource;

    private static final GameRepository INSTANCE = new GameRepository();

    /**
     * Private constructor
     */
    private GameRepository() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://18.191.105.81/fair_rummy_db");
        config.setUsername("pgadmin");
        config.setPassword("pgadmin@2025");

        this.dataSource = new HikariDataSource(config);

        this.jdbi = Jdbi.create(new ConnectionFactory() {
            @Override
            public Connection openConnection() throws SQLException {
                return dataSource.getConnection();
            }
        });
    }

    public static GameRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Save game data
     *
     * @param score Score data
     */
    public void save(@NonNull ScoreUpdate score) {

        jdbi.useTransaction(READ_COMMITTED, handle -> {

            handle.createUpdate("""
                            INSERT INTO public.games_history (
                                game_table_id,
                                game_joker_card_id,
                                game_date_time
                            ) VALUES (
                                :game_table_id,
                                :game_joker_card_id,
                                :game_date_time
                            )
                        """)
                  .attachToHandleForCleanup()
                  .bindBySqlType("game_table_id", score.getTableId(), INTEGER)
                  .bindBySqlType("game_joker_card_id", score.getJokerCardId(), VARCHAR)
                  .bindBySqlType("game_date_time", Timestamp.from(OffsetDateTime.now(ZoneOffset.UTC).toInstant()), TIMESTAMP_WITH_TIMEZONE)
                  .execute();

            Update update = handle.createUpdate("""
                            INSERT INTO public.games_history_players (
                                game_table_id,
                                game_player_id,
                                game_player_score,
                                game_player_status,
                                game_txn_amount,
                                game_cards
                            ) VALUES (
                                :game_table_id,
                                :game_player_id,
                                :game_player_score,
                                :game_player_status,
                                :game_txn_amount,
                                :game_cards
                            )
                        """)
                  .attachToHandleForCleanup()
                  .bindBySqlType("game_table_id", score.getTableId(), INTEGER);

            for (var userscore : score.getUserScores()) {

                String[][] gameCards = new String[userscore.getCardIds().size()][];
                for (int i = 0; i < userscore.getCardIds().size(); i++) {
                    gameCards[i] = userscore.getCardIds().get(i).toArray(new String[0]);
                }

                update
                      .bindBySqlType("game_player_id", userscore.getPlayingPlayerId(), INTEGER)
                      .bindBySqlType("game_player_score", userscore.getScore(), INTEGER)
                      .bindBySqlType("game_player_status", userscore.getStatus(), INTEGER)
                      .bindBySqlType("game_txn_amount", userscore.getTxnAmt() * 100, INTEGER)
                      .bindBySqlType("game_cards", gameCards, ARRAY)
                      .execute();
            }
        });
    }

    /**
     * Get game history
     *
     * @param playerId Player id
     * @param limit    Limit
     * @return List of score updates
     */
    public List<ScoreUpdate> history(long playerId, int limit) {

        return jdbi.inTransaction(READ_COMMITTED, handle -> handle
              .createCall("{ call public.proc_games_history(:game_player_id, :game_history_limit, :game_history_cursor) }")
              .attachToHandleForCleanup()
              .bindBySqlType("game_player_id", playerId, INTEGER)
              .bindBySqlType("game_history_limit", limit, INTEGER)
              .registerOutParameter("game_history_cursor", REF_CURSOR)
              .invoke(outParams -> {

                  var result = outParams.getRowSet("game_history_cursor")
                        .map(new GameHistoryRowMapper())
                        .collect(groupingBy(GameData::getTableId));

                  final List<ScoreUpdate> scoreUpdates = new ArrayList<>(limit);

                  for (var entry : result.entrySet()) {

                      var userScores = entry.getValue().stream()
                            .map(score -> UserScore.builder()
                                  .playingPlayerId(score.playerId)
                                  .score(score.playerScore)
                                  .status(score.playerStatus)
                                  .txnAmt(score.txnAmount)
                                  .cardIds(convertToList(score.cards))
                                  .build()
                            )
                            .toList();

                      scoreUpdates.add(new ScoreUpdate(entry.getKey(), "", userScores));
                  }

                  return scoreUpdates;
              }));
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }

    private static List<List<String>> convertToList(@NonNull String[][] cards) {
        return Arrays.stream(cards)
              .map(c -> Arrays.stream(c).toList())
              .toList();
    }

    /**
     * Game history row mapper
     */
    private static final class GameHistoryRowMapper implements RowMapper<GameData> {

        @Override
        public GameData map(ResultSet rs, StatementContext ctx) throws SQLException {

            GameData.Builder builder = GameData.builder();

            builder.tableId(rs.getInt("game_table_id"));
            builder.playerId(rs.getInt("game_player_id"));
            builder.playerScore(rs.getInt("game_player_score"));
            builder.playerStatus(rs.getInt("game_player_status"));
            builder.txnAmount(rs.getInt("game_txn_amount"));
            builder.cards((String[][]) rs.getArray("game_cards").getArray());

            return builder.build();
        }
    }

    @Data
    @Builder(builderClassName = "Builder")
    private static final class GameData {
        private final int tableId;
        private final int playerId;
        private final int playerScore;
        private final int playerStatus;
        private final int txnAmount;
        private final String[][] cards;
    }
}