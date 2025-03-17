package com.skillengine.rummy.message.handler;

import java.math.BigDecimal;

import com.skillengine.common.GameTemplates;
import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.globals.APIErrorCodes;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.TableCreation;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.service.CurrencyService;
import com.skillengine.sessions.PlayerSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableCreationHandler implements MessageHandler< TableCreation >
{

	private CurrencyService currencyService;

	/**
	 * @param currencyDetails
	 */
	public TableCreationHandler( CurrencyService currencyDetails )
	{
		this.currencyService = currencyDetails;
	}

	@Override
	public void handleMessage( PlayerSession session, TableCreation message, long receiverId )
	{
		long tableId = receiverId;
		long playerId = session.getUserID();
		if( tableId <= 0 )
		{
			ExitLobby lobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId" );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		GameTemplates gameTemplates = GameTemplates.builder().id( 10001 ).maxBuyin( 10000 ).maxPlayer( 6 ).minBuyin( 100 ).minPlayer( 2 ).noOfCards( 52 ).gameStartTime( 5000 )
				.cardsPerPlayer( 13 ).noOfDeck( 1 ).playerTurnTime( 20000 ).build();
		CurrencyDetails currencyDetails = currencyService.debit( new BoardJoinDetails( playerId, message.getTxnMoney(), BigDecimal.TWO, message.getTableId(), gameTemplates.getId() ) );
		if( currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE )
		{
			ExitLobby lobby = new ExitLobby( tableId, playerId, "Funds Unavailable" );
			log.info( "Null from the Fund Service {} TableId {}", playerId, tableId );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		RummyBoard board = new RummyBoard( tableId, gameTemplates );
		PlayerInfo info = new PlayerInfo( session.getUserID(), "tester-" + session.getUserID(), 1, currencyDetails.getDepositBucket(), currencyDetails.getWithdrawable(),
				currencyDetails.getNonWithdrawable() );
		boolean succ = board.joinPlayer( info, session );

	}

}
