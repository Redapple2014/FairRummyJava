package com.skillengine.rummy.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.skillengine.common.GameTemplates;
import com.skillengine.rummy.globals.GameGlobals;
import com.skillengine.rummy.globals.TimeTaskTypes;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.message.DealScoreCard;
import com.skillengine.rummy.message.DealsResult;
import com.skillengine.rummy.player.PlayerInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DealsRummyBoard extends RummyBoard
{
	private int currentDealNo;
	private Map< Long, Integer > playerRankMap;
	private AtomicBoolean isAllGamesCompleted = new AtomicBoolean( false );
	private AtomicBoolean isTieBreaker = new AtomicBoolean( false );
	private Map< Long, Integer > playerWinningCnt = new ConcurrentHashMap<>();

	public DealsRummyBoard( long tableId, GameTemplates templateDetails )
	{
		super( tableId, templateDetails );
		log.info( "DealsRummyBoard {}", templateDetails );

	}

	@Override
	protected synchronized void checkStatusOnJoin( boolean joinflag )
	{
		log.info( "changeStatusOnJoin : " + getAllplayer() + " TableId : " + getTableId() + " currentDealNo :" + currentDealNo + " gameStartFlag :" + gameStartFlag.get() + " !gameEndFlag  :"
				+ !gameEndFlag.get() + "   rummyDeal :" + rummyGame );
		try
		{
			switch( getAllplayer().size() )
			{
			case 0:
				log.info( "TableId : " + getTableId() + " changeStatusOnJoin PlayerSize : " + 0 );
				if( gameNo > 0 && !( currTask.getTaskType() == TimeTaskTypes.GAME_END && currTask != null ) )
				{
					setCompletedStatus();
				}
				else
				{
					log.info( "TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag" );
					gameStartFlag.set( false );
				}
				break;
			case 1:
				if( currentDealNo == 0 )
				{
					setStatus( GameGlobals.REGISTERING );
					if( currTask != null )
						currTask.cancel();
					if( gameStartFlag.get() && !gameEndFlag.get() && rummyGame != null )
					{
						log.info( "TableId : " + getTableId() + " Status : " + status + " Deal not yet ended" );
					}
					else
					{
						log.info( "TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag" );
						gameStartFlag.set( false );
					}
				}
				break;
			default:
				if( joinflag )
				{
					if( getAllplayer().size() == getGameTemplates().getMinPlayer() )
					{
						scheduleStartGame( getGameTemplates().getGameStartTime() );
					}
				}
				else if( currentDealNo == 0 && status == GameGlobals.STARTING && getAllplayer().size() < getGameTemplates().getMinPlayer() )
				{
					setStatus( GameGlobals.REGISTERING );
					if( currTask != null )
						currTask.cancel();
					if( gameStartFlag.get() && !gameEndFlag.get() && rummyGame != null )
					{
						log.info( "TableId : " + getTableId() + " Status : " + status + " Deal not yet ended" );
					}
					else
					{
						log.info( "TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag" );
						gameStartFlag.set( false );
					}
				}

				break;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean startGame()
	{
		if( currentDealNo == 0 && getAllplayer().size() < getGameTemplates().getMinPlayer() )
		{
			log.info( "TableId : " + getTableId() + " PlayerCount : " + ( getPlayingPlayerSize() + getSeatedPlayerSize() ) + " Not starting the game" );
			gameStartFlag.set( false );
		}
		else
		{
			super.startGame();
			if( gameStartFlag.get() )
			{
				++currentDealNo;
			}
			else if( currentDealNo > 0 && getAllplayer().size() == 1 )
			{

				endGameBeforeDealStart();
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	private boolean endGameBeforeDealStart()
	{
		try
		{

			List< PlayerInfo > playingPlayer = getPlayingPlayer();
			if( playingPlayer.size() > 1 )
			{
				return false;
			}
			long winner = playingPlayer.get( 0 ).getUserId();
			if( playerRankMap == null )
			{
				playerRankMap = new HashMap< Long, Integer >();
			}
			playerRankMap.put( winner, 1 );
			gameEndFlag.set( true );
			setGameEndStatus();
			gameStartFlag.set( false );
			gameEndFlag.set( false );
			rummyGame = null;
			if( status < GameGlobals.COMPLETED )
			{
				checkLastGameCriteria();
			}
		}
		catch( Throwable e )
		{
			e.printStackTrace();
		}
		return true;
	}

	protected void checkLastGameCriteria()
	{
		scheduleStartGame( getGameTemplates().getGameStartTime() );
		sendTableSeatMsg( 0 );
	}

	@Override
	public boolean endGame()
	{
		boolean status = super.endGame();
		if( currentDealNo == getGameTemplates().getDealsPerGame() && getGameTemplates().getVariantType() == VariantTypes.DEALS_RUMMY )
		{
			log.info( "All games are Ended {} currentDealNo {}", getTableId(), currentDealNo );
			isAllGamesCompleted.set( true );
		}
		long winnerId = getWinner();
		playerWinningCnt.merge( winnerId, 1, Integer::sum );
		if( !isAllGamesCompleted.get() )
		{
			checkForNextDeal();
			return false;
		}
		TieBreakerDetails breakerDetails = null;
		if( isAllGamesCompleted.get() && !isTieBreaker.get() )
		{
			breakerDetails = checkTieBreaker();
			if( breakerDetails != null && breakerDetails.isTieBreaker() )
			{
				isTieBreaker.set( true );
				List< Long > dealsBootOut = new ArrayList< Long >();
				List< Long > playersInTable = getAllplayer();
				for( Long plId : playersInTable )
				{
					if( !breakerDetails.tiebreakerEligiblePlayers().contains( plId ) )
					{
						dealsBootOut.add( plId );
					}
				}
				setTieBreaker( dealsBootOut );
				// Send New Message
				com.skillengine.rummy.message.TieBreakerDetails details = new com.skillengine.rummy.message.TieBreakerDetails( getTableId(), dealsBootOut, 2000 );
				getDispatcher().sendMessage( getAllplayer(), details );
				scheduleTieBreaker( 2000 );
				return false;
			}
		}
		if( isTieBreaker.get() )
		{
			// Announce the Winner
			if( winnerId <= 0 )
			{
				return false;
			}
			log.info( "TieBreaker Deal Winner tableId {} winnerId {}", getTableId(), winnerId );
			DealsResult dealsResult = new DealsResult( getTableId(), winnerId );
			getDispatcher().sendMessage( getAllplayer(), dealsResult );
			scheduleTableClose( 5000 );
			return false;
		}
		// If all the games completed and tie Breaker Not Eligible
		// log.info( "playerWinningCnt tableId {} playerWinningCnt {}",
		// getTableId(), playerWinningCnt );
		// int requiredWinningCnt = bestOfNMatch(
		// getGameTemplates().getDealsPerGame() );
		// boolean isWinnerAnnounced = false;
		// for( Long plId : playerWinningCnt.keySet() )
		// {
		// Integer cnt = playerWinningCnt.get( plId );
		// if( cnt >= requiredWinningCnt )
		// {
		// isWinnerAnnounced = true;
		// log.info( "Deal Winner tableId {} PlayerId {}", getTableId(),
		// plId );
		// DealsResult dealsResult = new DealsResult( getTableId(),
		// winnerId );
		// getDispatcher().sendMessage( getAllplayer(), dealsResult );
		// scheduleTableClose( 5000 );
		// break;
		// }
		// }
		// if( isWinnerAnnounced )
		// {
		// log.info( "Winner Already Informed so skipping tableId {} ",
		// getTableId() );
		// return false;
		// }
		// Check for the Score if the TieBreaker Not Enabled
		if( !isTieBreaker.get() )
		{
			chooseNonTieBreakerWinner();
		}
		return status;
	}

	public int bestOfNMatch( int n )
	{
		if( n == 2 || n == 6 )
		{
			return n == 2 ? 2 : 4;
		}
		return( ( n / 2 ) + 1 );
	}

	public void scheduleTableClose( long time )
	{
		try
		{
			if( currTask != null )
				currTask.cancel();
			currTask = new TableTimerTask( TimeTaskTypes.DEALS_END )
			{
				public void run()
				{
					setCompletedStatus();
				}
			};
			scheduleTask( currTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public void scheduleTieBreaker( long time )
	{
		try
		{
			if( currTask != null )
				currTask.cancel();
			currTask = new TableTimerTask( TimeTaskTypes.TIEBREAKER )
			{
				public void run()
				{
					checkForNextDeal();
				}
			};
			scheduleTask( currTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public DealScoreCard generateScoreCard()
	{
		return new DealScoreCard( getTableId(), getScoreByDeals() );
	}

	private TieBreakerDetails checkTieBreaker()
	{
		boolean isTieBreaker = false;
		TieBreakerDetails breakerDetails = null;
		List< Long > identicalPlayers = null;
		TreeMap< Integer, List< Long > > identicalScorePlayers = new TreeMap<>();
		Map< Long, Integer > scoreMap = getTotalScoreMap();
		for( Map.Entry< Long, Integer > entryScoreMap : scoreMap.entrySet() )
		{
			long userId = entryScoreMap.getKey();
			int score = entryScoreMap.getValue();
			identicalScorePlayers.computeIfAbsent( score, a -> new ArrayList< Long >() ).add( userId );
		}
		Entry< Integer, List< Long > > sortedIdentical = identicalScorePlayers.firstEntry();
		identicalPlayers = sortedIdentical.getValue();
		int scoreCnt = identicalPlayers.size();
		isTieBreaker = scoreCnt > 1 ? true : false;
		// int requiredWinningCnt = bestOfNMatch(
		// getGameTemplates().getDealsPerGame() );
		// for( Long plId : playerWinningCnt.keySet() )
		// {
		// Integer cnt = playerWinningCnt.get( plId );
		// if( cnt >= requiredWinningCnt )
		// {
		// isMaximumDealsWinningHappened = true;
		// }
		// }
		breakerDetails = new TieBreakerDetails( identicalPlayers, isTieBreaker );
		log.info( "Tiebreaker Details {} TableId {}", breakerDetails, getTableId() );
		return breakerDetails;

	}

	@Override
	public boolean userLeft( long playerId, int playerRequestedType )
	{
		boolean status = super.userLeft( playerId, playerRequestedType );
		if( leaveSeatReqPlayers.contains( playerId ) )
		{
			if( getAllplayer().size() == 1 )
			{
				chooseNonTieBreakerWinner();
			}
		}
		return status;
	}

	private record TieBreakerDetails( List< Long > tiebreakerEligiblePlayers, boolean isTieBreaker )
	{

	}

	private void chooseNonTieBreakerWinner()
	{
		Map< Long, Integer > scoreMap = getTotalScoreMap();
		TreeMap< Integer, Long > sortedScores = new TreeMap< Integer, Long >();
		for( Map.Entry< Long, Integer > entryScoreMap : scoreMap.entrySet() )
		{
			sortedScores.put( entryScoreMap.getValue(), entryScoreMap.getKey() );
		}
		Entry< Integer, Long > sortedEntry = sortedScores.firstEntry();
		long lowScoreWinner = sortedEntry.getValue();
		DealsResult dealsResult = new DealsResult( getTableId(), lowScoreWinner );
		getDispatcher().sendMessage( getAllplayer(), dealsResult );
		scheduleTableClose( 5000 );
	}

}
