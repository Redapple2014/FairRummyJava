package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.message.Declare;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class DeclareHandler implements MessageHandler<Declare> {

    @Override
    public void handleMessage(PlayerSession session, Declare message, long tableId) {
        if (tableId <= 0) {
            return;
        }
        RummyBoard board = (RummyBoard) ActiveBoards.getTable(tableId);
        if (board == null || board.getRummyGame() == null) {
            return;
        }
        board.getRummyGame().declare(message, session.getUserID());

    }

}
