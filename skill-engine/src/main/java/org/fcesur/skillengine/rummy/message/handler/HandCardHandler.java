package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.game.RummyGame;
import org.fcesur.skillengine.rummy.message.SetHandCards;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandCardHandler implements MessageHandler<SetHandCards> {

    @Override
    public void handleMessage(PlayerSession session, SetHandCards message, long tableId) {
        if (tableId <= 0) {
            return;
        }
        RummyBoard board = (RummyBoard) ActiveBoards.getTable(tableId);
        if (board == null) {
            return;
        }
        boolean isPresent = board.isPlayerAlreadyPresent(session.getUserID());
        if (isPresent) {
            log.info("Player is Playing HandCardHandler {}", message.getCards());
            RummyGame game = board.getRummyGame();
            if (game != null) {
                game.addHandCard(session.getUserID(), message.getCards());
                log.info("Received {}", game.getCustomGroupedCards(session.getUserID()));
            }
        }

    }

}
