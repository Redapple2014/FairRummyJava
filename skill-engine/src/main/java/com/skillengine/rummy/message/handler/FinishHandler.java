package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.Finish;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishHandler implements MessageHandler< Finish >
{

	@Override
	public void handleMessage( PlayerSession session, Finish message, long tableId )
	{
		if( tableId <= 0 )
		{
			log.error( "Invalid TableId {}", tableId );
			return;
		}
		RummyBoard rummyBoard = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( rummyBoard == null )
		{
			log.error( "Invalid rummyBoard {}", rummyBoard );
			return;
		}
		rummyBoard.getRummyGame().finish( message, session.getUserID() );

	}

}
