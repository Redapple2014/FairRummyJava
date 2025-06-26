package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.message.TableReconReq;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class TableReconnectionHandler implements MessageHandler<TableReconReq> {

    @Override
    public void handleMessage(PlayerSession session, TableReconReq message, long receiverId) {
        long tableId = receiverId;
        long playerId = session.getUserID();
        if (tableId <= 0) {
            ExitLobby exitLobby = new ExitLobby(tableId, playerId, "Table Is Invalid");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
            return;
        }
        RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
        if (rummyBoard == null) {
            ExitLobby exitLobby = new ExitLobby(tableId, playerId, "Table Is Closed/Invalid");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
            return;
        }
        boolean isPresent = rummyBoard.isPlayerAlreadyPresent(playerId);
        if (!isPresent) {
            ExitLobby exitLobby = new ExitLobby(tableId, playerId, "No Presence Of Player");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
            return;
        }
        rummyBoard.checkWhetherPlayerIsAlready(playerId);

    }

}
