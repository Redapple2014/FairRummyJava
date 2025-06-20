package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.BoardSetup;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;
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
