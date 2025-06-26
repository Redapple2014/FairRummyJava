package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.rummy.message.ExitLobby;
import org.fcesur.skillengine.rummy.message.LeaveBoard;
import org.fcesur.skillengine.rummy.table.RummyBoard;
import org.fcesur.skillengine.rummy.util.ActiveBoards;
import org.fcesur.skillengine.sessions.PlayerSession;

public class LeaveBoardHandler implements MessageHandler<LeaveBoard> {

    @Override
    public void handleMessage(PlayerSession session, LeaveBoard message, long tableId) {
        System.out.println("In Leave Table" + tableId);
        if (tableId <= 0) {
            ExitLobby lobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        RummyBoard board = (RummyBoard) ActiveBoards.getTable(tableId);
        if (board == null) {
            ExitLobby lobby = new ExitLobby(tableId, session.getUserID(), "Invalid TableId/Table is Closed`");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        boolean status = board.userLeft(session.getUserID(), 1);
        if (status) {
            ExitLobby lobby = new ExitLobby(tableId, session.getUserID(), "Player left`");
            SkillEngine.getInstance().getDispatcher().sendMessage(session, lobby);
            return;
        }
        System.out.println("status userLeft " + status);

    }

}
