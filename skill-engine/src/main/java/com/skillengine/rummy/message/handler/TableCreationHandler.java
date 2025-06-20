package com.skillengine.rummy.message.handler;

import java.math.BigDecimal;

import com.skillengine.common.GameTemplates;
import com.skillengine.dao.TableDetailsDAO;
import com.skillengine.dao.model.TableDetails;
import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.globals.APIErrorCodes;
import com.skillengine.rummy.globals.GameGlobals;
import com.skillengine.rummy.message.BoardStatus;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.TableCreation;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.service.CurrencyService;
import com.skillengine.sessions.PlayerSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableCreationHandler implements MessageHandler< TableCreation >
{

	private CurrencyService currencyService;

	public TableCreationHandler( CurrencyService currencyDetails )
	{
		this.currencyService = currencyDetails;
	}

	@Override
	public void handleMessage( PlayerSession session, TableCreation message, long receiverId )
	{
		long tableId = receiverId;
		long playerId = session.getUserID();
		GameTemplates gameTemplates = GameTemplates.builder().id( 10001 ).maxBuyin( 10000 ).maxPlayer( 6 ).minBuyin( 100 ).minPlayer( 2 ).noOfCards( 52 ).gameStartTime( 5000 )
				.cardsPerPlayer( 13 ).noOfDeck( 1 ).playerTurnTime( 5000 ).graceTime( 5000 ).pointValue( 1 ).variantType( 1 ).build();
		if( tableId <= 0 )
		{
			TableDetails details = new TableDetails();
			details.setTemplateId( gameTemplates.getId() );
			details.setStatus( GameGlobals.STARTING );
			tableId = SkillEngineImpl.getInstance().getTableDetailsDAO().insertTableDetails( details );
			if( tableId <= 0 )
			{
				ExitLobby lobby = new ExitLobby( tableId, session.getUserID(), "Invalid TableId" );
				SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
				return;
			}

		}
		CurrencyDetails currencyDetails = currencyService.debit( new BoardJoinDetails( playerId, message.getTxnMoney(), BigDecimal.TWO, message.getTableId(), gameTemplates.getId() ) );
		if( currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE )
		{
			ExitLobby lobby = new ExitLobby( tableId, playerId, "Funds Unavailable" );
			log.info( "Null from the Fund Service {} TableId {}", playerId, tableId );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, lobby );
			return;
		}
		RummyBoard board = new RummyBoard( tableId, gameTemplates );
		ActiveBoards.addTable( tableId, board );
		PlayerInfo info = new PlayerInfo( session.getUserID(), "tester-" + session.getUserID(), 1, currencyDetails.getDepositBucket(), currencyDetails.getWithdrawable(),
				currencyDetails.getNonWithdrawable() );
		boolean succ = board.joinPlayer( info, session );

	}

}
