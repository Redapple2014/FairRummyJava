package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.message.PickOpenDeck;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class PickOpenHandler implements MessageHandler<PickOpenDeck> {

    @Override
    public void handleMessage(PlayerSession session, PickOpenDeck message, long tableId) {
        long boardId = tableId;
        if (boardId <= 0) {
            ExitLobby exitLobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
            return;
        }
        RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
        boolean result = rummyBoard.getRummyGame().pickFromOpenDeck(message, session.getUserID());
        if (result) {
            ExitLobby exitLobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
        }

    }

}
