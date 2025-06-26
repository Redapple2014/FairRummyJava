package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.dto.BoardJoinDetails;
import org.fcesur.skillengine.dto.CurrencyDetails;
import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.globals.APIErrorCodes;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.message.PlayerTableJoin;
import org.fcesur.skillengine.rummy.player.PlayerInfo;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.service.CurrencyService;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PlayerJoinHandler implements MessageHandler<PlayerTableJoin> {

    private CurrencyService currencyService;

    public PlayerJoinHandler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void handleMessage(PlayerSession session, PlayerTableJoin message, long receiverId) {
        long tableId = receiverId;
        long playerId = session.getUserID();
        if (tableId <= 0 || playerId <= 0) {
            ExitLobby lobby = new ExitLobby(tableId, playerId, "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        RummyBoard board = (RummyBoard) ActiveBoards.getTable(tableId);
        if (board == null) {
            log.error("Board is null tableId {} PlayerID {}", tableId, playerId);
            ExitLobby lobby = new ExitLobby(tableId, playerId, "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        // Call the Fund Service for debiting the Funds [ Real
        // Currency ]
        CurrencyDetails currencyDetails = currencyService
              .debit(new BoardJoinDetails(playerId, message.getTxnMoney(), BigDecimal.TWO, message.getTableId(), board.getGameTemplates().getId()));
        if (currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE) {
            ExitLobby lobby = new ExitLobby(tableId, playerId, "Funds Unavailable");
            log.info("Null from the Fund Service {} TableId {}", playerId, tableId);
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        PlayerInfo info = new PlayerInfo(playerId, message.getPlrName(), 0, currencyDetails.getDepositBucket(), currencyDetails.getWithdrawable(), currencyDetails.getNonWithdrawable());
        boolean succ = board.joinPlayer(info, session);
        if (succ) {
            log.info("Player Joined Successfully tableId {} playerId {}", tableId, playerId);
        }
    }

}
