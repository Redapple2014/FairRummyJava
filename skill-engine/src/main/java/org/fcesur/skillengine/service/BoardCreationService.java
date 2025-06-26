package org.fcesur.skillengine.service;

import org.fcesur.skillengine.common.GameTemplates;
import org.fcesur.skillengine.dao.model.TableDetails;
import org.fcesur.skillengine.dto.BoardCreationInfo;
import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.globals.GameGlobals;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;

public class BoardCreationService {

    public BoardCreationInfo boardCreation(long templateId) {
        int maxPlayer = templateId == 1 ? 2 : 6;
        int noOfDeck = maxPlayer == 2 ? 1 : 2;
        GameTemplates gameTemplates = GameTemplates.builder().id((int) templateId).maxBuyin(10000).maxPlayer(maxPlayer).minBuyin(100).minPlayer(2).noOfCards(52).gameStartTime(20000)
              .cardsPerPlayer(13).noOfDeck(noOfDeck).playerTurnTime(25000).graceTime(10000).pointValue(1).build();
        TableDetails details = new TableDetails();
        details.setTemplateId(gameTemplates.getId());
        details.setStatus(GameGlobals.STARTING);
        long tableId = SkillEngine.getInstance().getTableDetailsDAO().insertTableDetails(details);
        RummyBoard board = new RummyBoard(tableId, gameTemplates);
        ActiveBoards.addTable(tableId, board);
        ActiveBoards.addTable(gameTemplates.getId(), tableId, System.currentTimeMillis());
        BoardCreationInfo boardCreationInfo = new BoardCreationInfo(tableId, "");
        return boardCreationInfo;

    }
}
