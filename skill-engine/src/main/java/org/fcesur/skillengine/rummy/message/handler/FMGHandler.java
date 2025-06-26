package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.common.GameTemplates;
import org.fcesur.skillengine.dao.TableDetailsDAO;
import org.fcesur.skillengine.dao.model.TableDetails;
import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.globals.GameGlobals;
import org.fcesur.skillengine.rummy.message.FMGRequest;
import org.fcesur.skillengine.rummy.message.FMGResponse;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class FMGHandler implements MessageHandler<FMGRequest> {

    private TableDetailsDAO dao = null;

    public FMGHandler(TableDetailsDAO dao) {
        this.dao = dao;
    }

    @Override
    public void handleMessage(PlayerSession session, FMGRequest message, long tableId) {
        if (tableId > 0) {
            return;
        }
        GameTemplates gameTemplates = GameTemplates.builder().id(10001).maxBuyin(10000).maxPlayer(6).minBuyin(100).minPlayer(2).noOfCards(52).gameStartTime(20000)
              .cardsPerPlayer(13).noOfDeck(1).playerTurnTime(25000).graceTime(10000).pointValue(1).variantType(1).build();
        long chosenBoardId = ActiveBoards.getTable(gameTemplates.getId());
        if (chosenBoardId > 0) {
            FMGResponse fmgResponse = new FMGResponse(chosenBoardId);
            SkillEngine.getInstance().getDispatcher().sendMessage(session, fmgResponse);
            return;
        }
        TableDetails details = new TableDetails();
        details.setTemplateId(gameTemplates.getId());
        details.setStatus(GameGlobals.STARTING);
        tableId = dao.insertTableDetails(details);
        RummyBoard board = new RummyBoard(tableId, gameTemplates);
        ActiveBoards.addTable(tableId, board);
        ActiveBoards.addTable(gameTemplates.getId(), tableId, System.currentTimeMillis());
        FMGResponse fmgResponse = new FMGResponse(tableId);
        SkillEngine.getInstance().getDispatcher().sendMessage(session, fmgResponse);
    }

}
