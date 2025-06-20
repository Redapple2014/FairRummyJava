package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.message.Discard;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class DiscardHandler implements MessageHandler< Discard >
{

	@Override
	public void handleMessage( PlayerSession session, Discard message, long tableId )
	{
		long boardId = tableId;
		if( boardId <= 0 )
		{
			ExitLobby exitLobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, exitLobby );
			return;
		}
		RummyBoard rummyBoard = ( RummyBoard ) ActiveBoards.getTable( tableId );
		boolean result = rummyBoard.getRummyGame().discard( message, session.getUserID() );
		if( result )
		{
			ExitLobby exitLobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, exitLobby );
		}

	}

}
