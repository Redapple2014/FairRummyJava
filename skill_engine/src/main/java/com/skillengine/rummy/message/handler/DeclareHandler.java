package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.Declare;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class DeclareHandler implements MessageHandler< Declare >
{

	@Override
	public void handleMessage( PlayerSession session, Declare message, long tableId )
	{
		if( tableId <= 0 )
		{
			return;
		}
		RummyBoard board = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( board == null || board.getRummyGame() == null )
		{
			return;
		}
		board.getRummyGame().declare( message, session.getUserID() );

	}

}
