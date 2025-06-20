package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.message.DiscardCardReq;
import com.skillengine.rummy.message.DiscardedCards;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

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
            SkillEngineImpl.getInstance().getDispatcher().sendMessage(session, discardedCards);

        } else {
            DiscardedCards discardedCards = new DiscardedCards(tableId, Collections.emptyList());
            SkillEngineImpl.getInstance().getDispatcher().sendMessage(session, discardedCards);
        }

    }

}
