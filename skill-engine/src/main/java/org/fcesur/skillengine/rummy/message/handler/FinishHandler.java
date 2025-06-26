package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.message.Finish;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishHandler implements MessageHandler<Finish> {

    @Override
    public void handleMessage(PlayerSession session, Finish message, long tableId) {
        if (tableId <= 0) {
            log.error("Invalid TableId {}", tableId);
            return;
        }
        RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
        if (rummyBoard == null) {
            log.error("Invalid rummyBoard {}", rummyBoard);
            return;
        }
        rummyBoard.getRummyGame().finish(message, session.getUserID());

    }

}
