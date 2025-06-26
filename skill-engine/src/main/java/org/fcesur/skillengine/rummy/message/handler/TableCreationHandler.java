package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.common.GameTemplates;
import org.fcesur.skillengine.dao.model.TableDetails;
import org.fcesur.skillengine.dto.BoardJoinDetails;
import org.fcesur.skillengine.dto.CurrencyDetails;
import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.globals.APIErrorCodes;
import org.fcesur.skillengine.rummy.globals.GameGlobals;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.message.TableCreation;
import org.fcesur.skillengine.rummy.player.PlayerInfo;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.service.CurrencyService;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TableCreationHandler implements MessageHandler<TableCreation> {

    private CurrencyService currencyService;

    public TableCreationHandler(CurrencyService currencyDetails) {
        this.currencyService = currencyDetails;
    }

    @Override
    public void handleMessage(PlayerSession session, TableCreation message, long receiverId) {
        long tableId = receiverId;
        long playerId = session.getUserID();
        GameTemplates gameTemplates = GameTemplates.builder().id(10001).maxBuyin(10000).maxPlayer(6).minBuyin(100).minPlayer(2).noOfCards(52).gameStartTime(5000)
              .cardsPerPlayer(13).noOfDeck(1).playerTurnTime(5000).graceTime(5000).pointValue(1).variantType(1).build();
        if (tableId <= 0) {
            TableDetails details = new TableDetails();
            details.setTemplateId(gameTemplates.getId());
            details.setStatus(GameGlobals.STARTING);
            tableId = SkillEngine.getInstance().getTableDetailsDAO().insertTableDetails(details);
            if (tableId <= 0) {
                ExitLobby lobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
                SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
                return;
            }

        }
        CurrencyDetails currencyDetails = currencyService.debit(new BoardJoinDetails(playerId, message.getTxnMoney(), BigDecimal.TWO, message.getTableId(), gameTemplates.getId()));
        if (currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE) {
            ExitLobby lobby = new ExitLobby(tableId, playerId, "Funds Unavailable");
            log.info("Null from the Fund Service {} TableId {}", playerId, tableId);
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        RummyBoard board = new RummyBoard(tableId, gameTemplates);
        ActiveBoards.addTable(tableId, board);
        PlayerInfo info = new PlayerInfo(session.getUserID(), "tester-" + session.getUserID(), 1, currencyDetails.getDepositBucket(), currencyDetails.getWithdrawable(),
              currencyDetails.getNonWithdrawable());
        boolean succ = board.joinPlayer(info, session);

    }

}
