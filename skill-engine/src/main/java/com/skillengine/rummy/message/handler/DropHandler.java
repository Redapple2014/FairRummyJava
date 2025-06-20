package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.Drop;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class DropHandler implements MessageHandler< Drop >
{

	@Override
	public void handleMessage( PlayerSession session, Drop message, long tableId )
	{
		if( tableId <= 0 )
		{
			return;
		}
		RummyBoard board = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( board == null )
		{

			return;
		}
		if( board.getRummyGame() == null )
		{
			return;
		}
		board.getRummyGame().drop( message, session.getUserID() );

	}

}
