package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.TableReconReq;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class TableReconnectionHandler implements MessageHandler< TableReconReq >
{

	@Override
	public void handleMessage( PlayerSession session, TableReconReq message, long userId )
	{
		long tableId = message.getTableId();
		long playerId = userId;
		if( tableId <= 0 )
		{
			ExitLobby exitLobby = new ExitLobby( tableId, playerId, "Table Is Invalid" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, exitLobby );
			return;
		}
		RummyBoard rummyBoard = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( rummyBoard == null )
		{
			ExitLobby exitLobby = new ExitLobby( tableId, playerId, "Table Is Closed/Invalid" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, exitLobby );
			return;
		}
		boolean isPresent = rummyBoard.isPlayerAlreadyPresent( playerId );
		if( !isPresent )
		{
			ExitLobby exitLobby = new ExitLobby( tableId, playerId, "No Presence Of Player" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, exitLobby );
			return;
		}
		rummyBoard.checkWhetherPlayerIsAlready( playerId );

	}

}
