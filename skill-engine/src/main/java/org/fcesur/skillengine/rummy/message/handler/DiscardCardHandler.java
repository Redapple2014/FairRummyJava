package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.message.DiscardCardReq;
import org.fcesur.skillengine.rummy.message.DiscardedCards;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

import java.util.Collections;
import java.util.List;

public class DiscardCardHandler implements MessageHandler<DiscardCardReq> {

    @Override
    public void handleMessage(PlayerSession session, DiscardCardReq message, long tableId) {
        if (tableId <= 0) {
            return;
        }
        RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
        if (rummyBoard != null && rummyBoard.getRummyGame() != null) {
            List<String> cardIds = rummyBoard.getRummyGame().getDiscardCards();
            DiscardedCards discardedCards = new DiscardedCards(tableId, cardIds);
            SkillEngine.getInstance().getDispatcher().sendMessage(session, discardedCards);

        } else {
            DiscardedCards discardedCards = new DiscardedCards(tableId, Collections.emptyList());
            SkillEngine.getInstance().getDispatcher().sendMessage(session, discardedCards);
        }

    }

}
