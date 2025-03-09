package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.LeaveBoard;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class LeaveBoardHandler implements MessageHandler< LeaveBoard >
{

	@Override
	public void handleMessage( PlayerSession session, LeaveBoard message, long tableId )
	{
		if( tableId <= 0 )
		{
			ExitLobby lobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		RummyBoard board = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( board == null )
		{
			ExitLobby lobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId/Table is Closed`" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		board.userLeft( session.getUserID(), 1 );

	}

}
