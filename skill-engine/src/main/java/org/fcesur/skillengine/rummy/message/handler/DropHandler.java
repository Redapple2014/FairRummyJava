package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.message.Drop;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class DropHandler implements MessageHandler<Drop> {

    @Override
    public void handleMessage(PlayerSession session, Drop message, long tableId) {
        if (tableId <= 0) {
            return;
        }
        RummyBoard board = (RummyBoard) ActiveBoards.getTable(tableId);
        if (board == null) {

            return;
        }
        if (board.getRummyGame() == null) {
            return;
        }
        board.getRummyGame().drop(message, session.getUserID());

    }

}
