package com.skillengine.rummy.message.handler;

import java.math.BigDecimal;

import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.globals.APIErrorCodes;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.PlayerTableJoin;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.service.CurrencyService;
import com.skillengine.sessions.PlayerSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerJoinHandler implements MessageHandler< PlayerTableJoin >
{

	private CurrencyService currencyService;

	public PlayerJoinHandler( CurrencyService currencyService )
	{
		this.currencyService = currencyService;
	}

	@Override
	public void handleMessage( PlayerSession session, PlayerTableJoin message, long receiverId )
	{
		long tableId = receiverId;
		long playerId = session.getUserID();
		if( tableId <= 0 || playerId <= 0 )
		{
			ExitLobby lobby = new ExitLobby( tableId, playerId, "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		RummyBoard board = ( RummyBoard ) ActiveBoards.getTable( tableId );
		if( board == null )
		{
			log.error( "Board is null tableId {} PlayerID {}", tableId, playerId );
			ExitLobby lobby = new ExitLobby( tableId, playerId, "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		// Call the Fund Service for debiting the Funds [ Real
		// Currency ]
		CurrencyDetails currencyDetails = currencyService
				.debit( new BoardJoinDetails( playerId, message.getTxnMoney(), BigDecimal.TWO, message.getTableId(), board.getGameTemplates().getId() ) );
		if( currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE )
		{
			ExitLobby lobby = new ExitLobby( tableId, playerId, "Funds Unavailable" );
			log.info( "Null from the Fund Service {} TableId {}", playerId, tableId );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		PlayerInfo info = new PlayerInfo( playerId, message.getPlrName(), 0, currencyDetails.getDepositBucket(), currencyDetails.getWithdrawable(), currencyDetails.getNonWithdrawable() );
		boolean succ = board.joinPlayer( info, session );
		if( succ )
		{
			log.info( "Player Joined Successfully tableId {} playerId {}", tableId, playerId );
		}
	}

}
