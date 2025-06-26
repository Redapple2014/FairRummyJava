package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.message.BoardSetup;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetupHandler implements MessageHandler<BoardSetup> {

    @Override
    public void handleMessage(PlayerSession session, BoardSetup message, long receiverId) {
        try {
            long tableId = receiverId;
            long playerId = session.getUserID();
            if (tableId <= 0) {
                return;
            }
            RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
            if (rummyBoard == null) {
                log.error("Rummy Board is null {}", tableId);
                return;
            }
            if (rummyBoard.isPlayerAlreadyPresent(playerId)) {
                log.info("Player Already there tableId {} , playerId {}", tableId, playerId);
                rummyBoard.checkWhetherPlayerIsAlready(session.getUserID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
