package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.message.Discard;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class DiscardHandler implements MessageHandler<Discard> {

    @Override
    public void handleMessage(PlayerSession session, Discard message, long tableId) {
        long boardId = tableId;
        if (boardId <= 0) {
            ExitLobby exitLobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
            return;
        }
        RummyBoard rummyBoard = (RummyBoard) ActiveBoards.getTable(tableId);
        boolean result = rummyBoard.getRummyGame().discard(message, session.getUserID());
        if (result) {
            ExitLobby exitLobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, exitLobby);
        }

    }

}
