package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngine;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.TableReconReq;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

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
