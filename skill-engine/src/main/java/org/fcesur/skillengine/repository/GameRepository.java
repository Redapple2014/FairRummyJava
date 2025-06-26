package org.fcesur.skillengine.repository;

import org.fcesur.skillengine.rummy.message.ScoreUpdate;
import org.fcesur.skillengine.rummy.message.ScoreUpdate2;
import org.fcesur.skillengine.rummy.message.UserScore2;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.sql.Types.ARRAY;
import static java.sql.Types.INTEGER;
import static java.sql.Types.REF_CURSOR;
import static java.sql.Types.TIMESTAMP_WITH_TIMEZONE;
import static java.sql.Types.VARCHAR;
import static org.jdbi.v3.core.transaction.TransactionIsolationLevel.READ_COMMITTED;

/**
 * Game repository
 */
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

    /**
     * Get game repository instance
     *
     * @return Instance of game repository
     */
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
                        INSERT INTO public.games_history (game_table_id, game_joker_card_id, game_date_time)
                            VALUES (:game_table_id, :game_joker_card_id, :game_date_time)
                        """)
                  .attachToHandleForCleanup()
                  .bindBySqlType("game_table_id", score.getTableId(), INTEGER)
                  .bindBySqlType("game_joker_card_id", score.getJokerCardId(), VARCHAR)
                  .bindBySqlType("game_date_time", OffsetDateTime.now(ZoneOffset.UTC), TIMESTAMP_WITH_TIMEZONE)
                  .execute();

            Update update = handle.createUpdate("""
                        INSERT INTO public.games_history_players (game_table_id, game_player_id, game_player_score, game_player_status, game_txn_amount, game_cards)
                            VALUES (:game_table_id, :game_player_id, :game_player_score, :game_player_status, :game_txn_amount, :game_cards)
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
    public @NonNull List<ScoreUpdate2> history(long playerId, int limit) {

        final List<TableData> tables = jdbi.inTransaction(READ_COMMITTED, handle -> handle
              .createCall("{ call public.proc_games_tables_by_player(:games_player_id, :games_history_limit, :games_tables_cursor) }")
              .attachToHandleForCleanup()
              .bindBySqlType("games_player_id", playerId, INTEGER)
              .bindBySqlType("games_history_limit", limit, INTEGER)
              .registerOutParameter("games_tables_cursor", REF_CURSOR)
              .invoke(outParams -> {
                  return outParams.getRowSet("games_tables_cursor")
                        .map(new GameTableRowMapper())
                        .collect(Collectors.toList());
              })
        );

        final List<ScoreUpdate2> scoreUpdates = new ArrayList<>(tables.size());

        for (var table : tables) {

            final List<UserScore2> scores = jdbi.inTransaction(READ_COMMITTED, handle -> handle
                  .createCall("{ call public.proc_games_tables_players(:games_table_id, :games_table_players_cursor) }")
                  .attachToHandleForCleanup()
                  .bindBySqlType("games_table_id", table.getTableId(), INTEGER)
                  .registerOutParameter("games_table_players_cursor", REF_CURSOR)
                  .invoke(outParams -> {

                      return outParams.getRowSet("games_table_players_cursor")
                            .map(new GamePlayerRowMapper())
                            .map(player -> UserScore2.builder()
                                  .id(player.getId())
                                  .score(player.getScore())
                                  .status(player.getStatus())
                                  .amount(player.getTxnAmount())
                                  .cards(convertToList(player.getCards()))
                                  .build())
                            .collect(Collectors.toList());
                  })
            );

            scoreUpdates.add(ScoreUpdate2.builder()
                  .tableId(table.getTableId())
                  .jokerCardId(table.getJokerCardId())
                  .scores(scores)
                  .build()
            );
        }

        return scoreUpdates;
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
     * Game table row mapper
     */
    private static final class GameTableRowMapper implements RowMapper<TableData> {

        @Override
        public TableData map(ResultSet rs, StatementContext ctx) throws SQLException {
            return TableData.builder()
                  .tableId(rs.getInt("game_table_id"))
                  .jokerCardId(rs.getString("game_joker_card_id"))
                  .dateTime(OffsetDateTime.ofInstant(rs.getTimestamp("game_date_time").toInstant(), ZoneOffset.UTC))
                  .build();
        }
    }

    /**
     * Game player row mapper
     */
    private static final class GamePlayerRowMapper implements RowMapper<PlayerData> {

        @Override
        public PlayerData map(ResultSet rs, StatementContext ctx) throws SQLException {
            return PlayerData.builder()
                  .id(rs.getInt("game_player_id"))
                  .score(rs.getInt("game_player_score"))
                  .status(rs.getInt("game_player_status"))
                  .txnAmount(rs.getInt("game_txn_amount"))
                  .cards((String[][]) rs.getArray("game_cards").getArray())
                  .build();
        }
    }

    @Data
    @Builder(builderClassName = "Builder")
    private static final class TableData {
        private final int tableId;
        private final String jokerCardId;
        private final OffsetDateTime dateTime;
    }

    @Data
    @Builder(builderClassName = "Builder")
    private static final class PlayerData {
        private final int id;
        private final int score;
        private final int status;
        private final int txnAmount;
        private final String[][] cards;
    }
}