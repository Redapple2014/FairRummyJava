package com.skillengine.rummy.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.skillengine.common.GameTemplates;
import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.dto.ExitDetails;
import com.skillengine.dto.PointSettlementInfo;
import com.skillengine.dto.SettlementCurrencyInfo;
import com.skillengine.dto.TableStatusInfo;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.game.GameStateChanges;
import com.skillengine.rummy.game.RummyGame;
import com.skillengine.rummy.game.UserHandInfo;
import com.skillengine.rummy.globals.APIErrorCodes;
import com.skillengine.rummy.globals.GameGlobals;
import com.skillengine.rummy.globals.TimeTaskTypes;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.message.BoardInfo;
import com.skillengine.rummy.message.BoardStatus;
import com.skillengine.rummy.message.Converter;
import com.skillengine.rummy.message.ExitLobby;
import com.skillengine.rummy.message.Message;
import com.skillengine.rummy.message.MessageDispatcher;
import com.skillengine.rummy.message.ScoreUpdate;
import com.skillengine.rummy.message.Seats;
import com.skillengine.rummy.message.UpdateSeatBalance;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.player.SeatPlayerInfo;
import com.skillengine.rummy.settlement.PointsSettlement;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.service.CurrencyService;
import com.skillengine.service.CurrencyServiceImpl;
import com.skillengine.sessions.PlayerSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RummyBoard extends Board
{

	protected Object tableLock = new Object();
	private Timer tableTimer = null;
	protected TableTimerTask currTask;
	protected boolean tableCompletedStatus = false;
	protected List< Long > leaveSeatReqPlayers = null;
	protected int gameNo = 0;
	protected AtomicBoolean tossRequired = new AtomicBoolean( false );
	private MessageDispatcher dispatcher = null;
	protected AtomicBoolean gameStartFlag = new AtomicBoolean( false );
	protected AtomicBoolean gameEndFlag = new AtomicBoolean( false );
	private CurrencyService currencyService;
	private Map< Long, PlayerInfo > userCurrencyMap = new ConcurrentHashMap<>();
	private Map< Long, CurrencyDetails > userCurrencyDetails = new ConcurrentHashMap<>();
	private long gameStartTime = 0l;
	private List< Long > orderedPlayers = new ArrayList< Long >();
	protected RummyGame rummyGame;
	protected long dealStartTime = -1;
	private AtomicInteger tableIdDealer = new AtomicInteger( 0 );
	private ScoreUpdate scoreUpdate;
	private long gameEndTime = 0l;
	private long winnerId = 0;

	public RummyBoard( long tableId, GameTemplates templateDetails )
	{
		super( tableId, templateDetails );
		try
		{

			tableTimer = new Timer( "Board-" + tableId, true );
			setStatus( GameGlobals.TABLE_OPEN );
			dispatcher = new MessageDispatcher( SkillEngineImpl.getInstance().getJackson() );
			currencyService = new CurrencyServiceImpl();
			ActiveBoards.addTable( tableId, this );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	protected void setStatus( int status )
	{
		try
		{
			if( this.status == GameGlobals.COMPLETED )
			{
				return;
			}
			this.status = status;

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	@Override
	public boolean startGame()
	{
		try
		{

			winnerId = 0;
			if( checkPlayersAtStartGame() )
			{
				setStatus( GameGlobals.IN_PROGRESS );
				sendBoardStatusMsg( 0 );
				leaveSeatReqPlayers = new ArrayList< Long >();
				if( getSeatedPlayerSize() > 0 )
				{
					addAllSeatedToPlaying();
				}
				getPlayingPlayer().clear();
				List< PlayerInfo > finalJoinedPlayers = new ArrayList<>();
				if( !tossRequired.get() && orderedPlayers.size() > 0 )
				{
					for( long playerId : orderedPlayers )
					{
						SeatInfo seat = getSeat( playerId );
						if( seat != null )
						{
							SeatPlayerInfo p = seat.getPlayer();
							finalJoinedPlayers.add( userCurrencyMap.get( playerId ) );
						}
						else
						{
							finalJoinedPlayers.add( null );
						}
					}

					int dealerIndex = 0;
					long dealer = orderedPlayers.get( dealerIndex );
					SeatInfo dealerSeat = getSeat( dealer );
					if( dealerSeat != null && dealerSeat.getState() == 1 )
					{
						dealerIndex = 0;

					}
					else
					{
						dealerIndex = orderedPlayers.size();
						do
						{
							long prevDealer = orderedPlayers.get( --dealerIndex );
							SeatInfo prevDealerSeat = getSeat( prevDealer );
							if( prevDealerSeat != null && prevDealerSeat.getState() == 1 )
							{
								break;
							}
						}
						while( dealerIndex > 0 );
					}
					getPlayingPlayer().addAll( finalJoinedPlayers.subList( dealerIndex, finalJoinedPlayers.size() ) );
					if( dealerIndex > 0 )
					{
						getPlayingPlayer().addAll( finalJoinedPlayers.subList( 0, dealerIndex ) );
					}
				}
				else
				{
					List< SeatInfo > seats = getSeats();

					for( int i = 0; i < seats.size(); i++ )
					{
						SeatInfo seat = seats.get( i );
						SeatPlayerInfo p = seat.getPlayer();
						if( p == null )
						{
							continue;
						}
						finalJoinedPlayers.add( userCurrencyMap.get( p.getUserId() ) );
					}
					getPlayingPlayer().addAll( finalJoinedPlayers );
				}
				Iterator< PlayerInfo > playingPlayerItr = getPlayingPlayer().iterator();
				while( playingPlayerItr.hasNext() )
				{

					PlayerInfo player = playingPlayerItr.next();
					if( player == null )
					{
						playingPlayerItr.remove();
					}
				}

				List< PlayerInfo > currentPlayers = getPlayingPlayer();
				List< Long > currentPlayerIds = new ArrayList< Long >();
				for( PlayerInfo player : currentPlayers )
				{
					Long playerId = player.getUserId();
					currentPlayerIds.add( playerId );
					log.info( "TableId : " + getTableId() + " PlayerId : " + player.getUserId() + " Bal at GameStart : " + userCurrencyMap.get( playerId ) );
				}
				log.info( "TableId : " + getTableId() + " Players at GameStart : " + currentPlayerIds + " GameNo : " + gameNo + "  tossRequired  :" + tossRequired.get()
						+ " OrderedPlayer :" + getOrderedPlayers() + " GameNo : " + gameNo + "  Playing Player :" + getAllplayer() );
				setTossRequired();
				boolean toss = tossRequired.get();
				tossRequired.set( false );
				rummyGame = new RummyGame( this, currentPlayers, toss );
				TimerTask gameStartTask = new TimerTask()
				{
					@Override
					public void run()
					{
						rummyGame.startAt( null );
					}
				};
				tableTimer.schedule( gameStartTask, 300 );
				dealStartTime = System.currentTimeMillis();
				gameNo++;
			}
			else
			{
				log.info( "TableId : " + getTableId() + " PlayerCount : " + ( getPlayingPlayerSize() + getSeatedPlayerSize() ) + " Not starting the game" );
				gameStartFlag.set( false );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean endGame()
	{
		try
		{
			gameEndFlag.set( true );
			setGameEndStatus();
			log.info( "TableId : " + getTableId() + " endGame : Status" + getStatus() );
			trackTableStatus();
			if( getRummyGame() == null )
			{
				log.error( "TableId : " + getTableId() + " Game is NULL" );
				return true;
			}
			Map< Long, UserHandInfo > playerMap = getRummyGame().getUserHandInfo();
			Iterator< Long > playerIdItr = playerMap.keySet().iterator();
			long winner = rummyGame.getWinner();
			winnerId = rummyGame.getWinner();
			while( playerIdItr.hasNext() )
			{
				long playerId = playerIdItr.next();
				SeatInfo seatInfo = getSeat( playerId );
				if( playerId == winner )
				{
					continue;
				}
			}

			log.info( "TableId : " + getTableId() + " Winner : " + winner + " PlayerScoreMap : " + rummyGame.getScoreMap() + "variantType : " + getGameTemplates().getVariantType() );
			if( getGameTemplates().getVariantType() == VariantTypes.POINTS_RUMMY )
			{
				pointSettlement();
			}
			log.info( "TableId :" + getTableId() + " Status : " + status + "..endGame()" );
			gameStartFlag.set( false );
			gameEndFlag.set( false );
			gameEndTime = System.currentTimeMillis();
			rummyGame = null;
			if( getGameTemplates().getVariantType() == VariantTypes.DEALS_RUMMY )
			{
				return true;
			}
			if( status != GameGlobals.COMPLETED )
			{
				checkForNextDeal();
			}
			if( currTask != null && currTask.getTaskType() == TimeTaskTypes.GAME_END )
			{
				currTask = null;
			}
			if( getAllplayer().size() == 0 )
			{
				setCompletedStatus();
			}
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}

	}

	private SettlementCurrencyInfo pointSettlement()
	{
		try
		{
			PointsSettlement pointsSettlement = new PointsSettlement();
			PointSettlementInfo pointSettlementInfo = PointSettlementInfo.builder().playerVsScore( rummyGame.getScoreMap() ).winnerId( rummyGame.getWinner() )
					.pointValue( getGameTemplates().getPointValue() ).serviceFee( getGameTemplates().getServiceFee() ).build();
			SettlementCurrencyInfo info = pointsSettlement.settlement( pointSettlementInfo );
			if( info == null )
			{
				return null;
			}
			Map< Long, BigDecimal > losingInfo = info.getUserCurrencyDetails();
			for( Long losingPlayerId : losingInfo.keySet() )
			{
				BigDecimal losingAmt = losingInfo.get( losingPlayerId );
				if( info.getWinnerId() == losingPlayerId )
				{
					CurrencyDetails winningDetails = userCurrencyDetails.get( losingPlayerId );
					winningDetails.setWithdrawable( winningDetails.getWithdrawable().add( losingAmt ) );
					userCurrencyDetails.put( losingPlayerId, winningDetails );
					continue;
				}
				calculateSeatBalance( losingPlayerId, losingAmt );
			}
			for( Long settledPlayers : info.getUserCurrencyDetails().keySet() )
			{
				updateTableSeatBalance( settledPlayers, info.getWinnerId() == settledPlayers );
			}
			return info;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}

	private void updateTableSeatBalance( long playerId, boolean isWinner )
	{
		CurrencyDetails currencyDetails = userCurrencyDetails.get( playerId );
		SeatInfo seat = getSeat( playerId );
		if( seat == null )
		{

			return;
		}
		seat.setSeatPlayerBalance( currencyDetails.getDepositBucket().add( currencyDetails.getWithdrawable() ).add( currencyDetails.getNonWithdrawable() ) );
		UpdateSeatBalance balance = new UpdateSeatBalance( getTableId(), seat.getSeatPlayerBalance(), playerId, isWinner );
		dispatcher.sendMessage( getAllplayer(), balance );

	}

	protected void checkForNextDeal()
	{
		if( checkPlayersAtStartGame() )
		{
			scheduleStartGame( getGameTemplates().getGameStartTime() );
		}
		else
		{
			setStatus( GameGlobals.REGISTERING );
		}
		sendBoardStatusMsg( 0 );
	}

	@Override
	public void setLastGame( boolean isLastGame )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean joinPlayer( PlayerInfo playerInfo, PlayerSession playerSession )
	{
		try
		{
			boolean succ = false;
			long playerId = playerInfo.getUserId();
			if( ( status == GameGlobals.COMPLETED || tableCompletedStatus ) )
			{
				return false;
			}
			if( isPlayerAlreadyPresent( playerId ) )
			{
				log.debug( "PlayerId : " + playerId + " tableId : " + getTableId() + " already in the Board" );
				checkWhetherPlayerIsAlready( playerId );
				return true;
			}
			CurrencyDetails currencyDetails = currencyService
					.debit( new BoardJoinDetails( playerId, new BigDecimal( getGameTemplates().getMaxBuyin() ), BigDecimal.TWO, getTableId(), getGameTemplates().getId() ) );
			if( currencyDetails == null || currencyDetails.getStatus() == APIErrorCodes.FAILURE )
			{
				ExitLobby lobby = new ExitLobby( getTableId(), playerId, "Funds Unavailable" );
				dispatcher.sendMessage( playerSession, lobby );
				return false;
			}
			userCurrencyDetails.put( playerId, currencyDetails );
			userCurrencyMap.put( playerId, playerInfo );
			int id = takeAnySeat( playerInfo );
			if( id == -1 )
			{
				log.debug( " Join Issue no empty seat available" + playerId + "::" + getTableId() );
				ExitLobby lobby = new ExitLobby( getTableId(), playerInfo.getUserId(), "JOINISSUE" );
				dispatcher.sendMessage( playerSession, lobby );
				return succ;
			}
			else
			{
				dispatcher.addBlockMsg( playerId );
				dispatcher.addSession( playerSession );
				joinPlayerUpdates( playerId );
				dispatcher.removeBlock( playerId );
				sendTableSeatMsg( playerId );
				succ = true;
				trackTableStatus();
				log.debug( "TableId :" + getTableId() + " new Player added: " + playerId + "..joinPlayer " );
			}

			if( succ )
			{
				tossRequired.set( true );
				if( getAvlSeat() <= 0 )
				{
					ActiveBoards.removeTable( getGameTemplates().getId(), getTableId() );
				}
			}
			return succ;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return false;
	}

	public void checkWhetherPlayerIsAlready( long playerId )
	{
		try
		{
			List< Message > msgList = new ArrayList< Message >();
			log.debug( "player is already there " + playerId + " TableId : " + getTableId() );
			BoardInfo boardInfo = new BoardInfo( getGameTemplates().getMaxPlayer(), getGameTemplates().getMinBuyin(), getGameTemplates().getMaxBuyin(), getTableId(),
					getGameTemplates().getNoOfCards(), gameNo, getGameTemplates().getId() );
			msgList.add( boardInfo );
			Seats seats = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), playerId ) );
			msgList.add( seats );
			BoardStatus state = getBoardStatus( playerId );
			if( state != null )
			{
				msgList.add( state );
			}
			if( status != GameGlobals.COMPLETED )
			{

				if( rummyGame != null )
				{
					log.debug( "game is in progress player join" + playerId + " " + getTableId() );
					List< Message > dealMsgs = rummyGame.sendReconDealMsg( playerId, true );
					if( dealMsgs != null && dealMsgs.size() > 0 )
					{
						msgList.addAll( dealMsgs );
					}
				}

			}
			dispatcher.sendMessage( playerId, msgList );

			synchronized( tableLock )
			{
				if( leaveSeatReqPlayers != null && leaveSeatReqPlayers.contains( playerId ) )
					leaveSeatReqPlayers.remove( playerId );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	@Override
	public boolean userLeft( long playerId, int playerRequestedType )
	{
		boolean succ = false;
		log.info( "TableId: {} PlayerId: {}  Table Status: {}" + getTableId(), playerId, getStatus() );
		if( status == GameGlobals.COMPLETED )
		{
			return true;
		}
		boolean playerReqFlag = playerRequestedType == 1 ? true : false;
		boolean bootOut = playerRequestedType == 2 ? true : false;
		boolean dealEnd = rummyGame != null ? rummyGame.getGameState() == GameStateChanges.COMPLETED ? true : false : false;
		if( rummyGame != null && gameStartFlag.get() && !gameEndFlag.get() )
		{
			log.info( "Entering the Rummy Game {} gameStartFlag{} gameEndFlag{}", getTableId(), gameStartFlag.get(), gameEndFlag.get() );
			synchronized( tableLock )
			{
				if( leaveSeatReqPlayers != null )
				{
					if( leaveSeatReqPlayers.contains( playerId ) )
					{
						if( getSeat( playerId ) != null )
						{
							removePlayer( playerId, 0, playerReqFlag, bootOut );
							log.info( "TableId : " + getTableId() + " PlayerId : " + playerId + " Player already again and left" );
						}
						log.info( "TableId : " + getTableId() + " PlayerId : " + playerId + " Player already left" );
						return true;
					}
					else
					{
						leaveSeatReqPlayers.add( playerId );
					}
				}
			}
			int typeOfUser = whatTypeOfPlayer( playerId );
			if( typeOfUser == 2 && playerReqFlag && !dealEnd )
			{
				log.info( "Rummy Game Process User left {} typeOfUser {} dealEnd{} ", getTableId(), typeOfUser, dealEnd );
				succ = rummyGame.processUserLeft( playerId );
			}
			else
			{
				succ = true;
			}
			if( succ && typeOfUser == 2 )
			{
				// Get the updated Balance
				CurrencyDetails playerInfo = userCurrencyDetails.getOrDefault( playerId, null );
				if( playerInfo == null )
				{
					log.info( "TableID: {} playerID {} currency is Null{}", getTableId(), playerId );
					return false;
				}
				currencyService.credit( new ExitDetails( playerId, playerInfo.getNonWithdrawable(), playerInfo.getWithdrawable(), playerInfo.getDepositBucket(), getTableId() ) );
			}
			removePlayer( playerId, 0, playerReqFlag, false );
			return true;
		}
		CurrencyDetails playerInfo = userCurrencyDetails.getOrDefault( playerId, null );
		if( playerInfo == null )
		{
			log.info( "TableID: {} playerID {} currency is Null{}", getTableId(), playerId );
			return false;
		}
		currencyService.credit( new ExitDetails( playerId, playerInfo.getNonWithdrawable(), playerInfo.getWithdrawable(), playerInfo.getDepositBucket(), getTableId() ) );
		removePlayer( playerId, 0, playerReqFlag, false );
		trackTableStatus();
		return true;
	}

	public int takeAnySeat( PlayerInfo info )
	{
		int seatid = -1;
		synchronized( this )
		{
			if( isPlayerAlreadyPresent( info.getUserId() ) )
			{
				SeatInfo seat = getSeat( info.getUserId() );
				if( seat != null )
				{
					seatid = seat.getId();
				}
			}
			if( seatid < 0 )
			{
				try
				{

					for( int i = 0; i < seats.size(); i++ )
					{
						SeatInfo seat = seats.get( i );
						if( seat.getState() == 0 )
						{
							seat.setPlayer( new SeatPlayerInfo( info.getUserId(), info.getUserName(), info.getAvatarId() ) );
							seat.setState( 1 );
							PlayerInfo currencyDetails = userCurrencyMap.get( info.getUserId() );
							seat.setSeatPlayerBalance(
									currencyDetails.getDepositBalance().add( currencyDetails.getWithdrawable() ).add( currencyDetails.getNonWithdrawable() ) );
							seatid = seat.getId();
							addSeatedPlayer( info );
							break;
						}
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}

		return seatid;
	}

	private BoardStatus getBoardStatus( long playerId )
	{
		long time = 0;
		if( status == GameGlobals.STARTING )
		{
			time = getGameTemplates().getGameStartTime();
			if( currTask != null && currTask.getTaskType() == TimeTaskTypes.GAME_START && playerId > 0)
			{
				time = ( long ) currTask.scheduledExecutionTime() - System.currentTimeMillis();
				time = time > 0 ? time : 0;
			}
		}
		else if( status == GameGlobals.REGISTERING && gameNo > 0 && rummyGame != null )
		{
			log.info( "REGISTERING as the game is on TableId : " + getTableId() );
			return null;
		}
		BoardStatus boardStatus = new BoardStatus( getTableId(), status, ( int ) time );
		return boardStatus;
	}

	protected boolean joinPlayerUpdates( long playerId )
	{
		long gameId = 0;
		if( rummyGame != null )
		{
			gameId = rummyGame.getGameId();

		}
		long amount = getGameTemplates().getMaxBuyin();
		List< Message > msgList = new ArrayList< Message >();
		boolean succ = true;
		try
		{
			BoardInfo boardInfo = new BoardInfo( getGameTemplates().getMaxPlayer(), getGameTemplates().getMinBuyin(), getGameTemplates().getMaxBuyin(), getTableId(),
					getGameTemplates().getNoOfCards(), gameNo, getGameTemplates().getId() );
			msgList.add( boardInfo );
			sendTableSeatMsg( 0 );
			Seats seats = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), playerId ) );
			msgList.add( seats );
			BoardStatus state = getBoardStatus( playerId );
			msgList.add( state );
			getTableState( playerId, msgList );
			dispatcher.sendMessageWithOutBlockCheck( playerId, msgList );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return succ;
	}

	protected void getTableState( long playerId, List< Message > msgList )
	{
		if( status == GameGlobals.IN_PROGRESS )
		{
			BoardStatus boardStatus = getBoardStatus( playerId );
			if( boardStatus != null )
			{
				msgList.add( boardStatus );
			}

			if( rummyGame != null )
			{
				List< Message > dealMsgs = rummyGame.sendReconDealMsg( playerId, true );
				if( dealMsgs != null && dealMsgs.size() > 0 )
				{
					msgList.addAll( dealMsgs );
				}
			}
		}
		else
		{
			int oldStatus = status;
			checkStatusOnJoin( true );
			if( oldStatus != status )
			{
				sendBoardStatusMsg( 0 );
			}
			BoardStatus boardStatus = getBoardStatus( playerId );
			if( boardStatus != null )
			{
				msgList.add( boardStatus );
			}

		}
	}

	protected void sendTableSeatMsg( long playerId )
	{
		try
		{

			if( playerId > 0 )
			{
				Seats seats = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), playerId ) );
				dispatcher.sendMessage( playerId, seats );
			}
			else
			{
				List< Long > playerList = getAllplayer();
				System.out.println( "playerList" + playerList );
				for( long seatPlayerId : playerList )
				{
					if( seatPlayerId != playerId )
					{
						Seats seatInfo = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), playerId ) );
						dispatcher.sendMessage( seatPlayerId, seatInfo );
					}
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	protected void removePlayer( Long playerId, long gameId, boolean playerRequested, boolean bootout )
	{
		try
		{
			boolean isPlayerInTable = isPlayerInTable( playerId );
			SeatPlayerInfo playerInfo = null;
			synchronized( tableLock )
			{
				SeatInfo st = getSeat( playerId );
				BigDecimal amount = BigDecimal.ZERO;
				int playerType = 0;
				if( st != null )
				{
					playerInfo = st.getPlayer();
					amount = getTableSeatBal( st );
					playerType = whatTypeOfPlayer( playerId );
					boolean status = removePlayerSeat( playerId );
					log.info( "TableId : " + getTableId() + " PlayerId : " + playerId + " RemovePlayerStatus : " + status + " playerRequested :" + playerRequested
							+ "  isPlayerInTable  :" + isPlayerInTable );
				}
				else
				{
					log.error( "TableSeat is null for playerId: {} tableId: {} " + playerId, getTableId() );
				}
			}
			if( !playerRequested )
			{
				log.info( "TakeToLobby TableId{} PlayerId {} ", playerId, getTableId() );
				boolean status[] = { false, false };
				if( isPlayerInTable )
				{
					List< Message > listMsg = new ArrayList< Message >();
					ExitLobby lobby = new ExitLobby( getTableId(), playerId, "BOOTOUT" );
					listMsg.add( lobby );
					dispatcher.sendMessage( playerId, listMsg );
				}
			}
			checkStatusOnJoin( false );
			BoardStatus state = getBoardStatus( playerId );
			if( state != null )
			{
				List< Long > playerList = getAllplayer();
				for( long seatPlayerId : playerList )
				{
					List< Message > msgList = new ArrayList< Message >();
					msgList.add( state );
					Seats seats = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), seatPlayerId ) );
					msgList.add( seats );
					dispatcher.sendMessage( seatPlayerId, msgList );
				}
			}
			else
			{
				List< Long > playerList = getAllplayer();
				for( long seatPlayerId : playerList )
				{
					Seats seats = new Seats( getTableId(), Converter.convertSeatsList( getSeats(), seatPlayerId ) );
					dispatcher.sendMessage( seatPlayerId, seats );
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	protected BigDecimal getTableSeatBal( SeatInfo playerSeat )
	{
		return playerSeat.getSeatPlayerBalance();
	}

	protected boolean isPlayerInTable( long playerId )
	{
		return getAllplayer().contains( playerId );
	}

	protected synchronized void checkStatusOnJoin( boolean joinflag )
	{
		log.info( "checkStatusOnJoin : " + getAllplayer() + " TableId : " + getTableId() );
		try
		{
			switch( getAllplayer().size() )
			{
			case 0:
				log.info( "TableId : " + getTableId() + " changeStatusOnJoin PlayerSize : " + 0 );
				if( gameNo > 0 )
				{
					if( currTask != null && currTask.getTaskType() == TimeTaskTypes.GAME_END )
					{
						log.info( "TableId : " + getTableId() + " endGameTask is scheduled" );
					}
					else if( gameStartFlag.get() && !gameEndFlag.get() )
					{
						log.info( "TableId : " + getTableId() + " endGame not happened" );
					}
					else
					{
						setCompletedStatus();
					}
				}
				else
				{
					log.info( "TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag" );
					gameStartFlag.set( false );
				}
				break;
			case 1:
				setStatus( GameGlobals.REGISTERING );
				if( currTask != null && currTask.getTaskType() != TimeTaskTypes.GAME_END )
					currTask.cancel();
				// All Users except one, leaves before the
				// game starts, we need to reset the
				// gameStartFlag
				if( gameStartFlag.get() && !gameEndFlag.get() && rummyGame != null )
				{
					log.info( "TableId : " + getTableId() + " Status : " + status + " Deal not yet ended" );
				}
				else
				{
					log.info( "TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag" );
					gameStartFlag.set( false );

				}
				break;
			default:
				if( joinflag )
				{
					if( getAllplayer().size() >= getGameTemplates().getMinPlayer() )
					{
						scheduleStartGame( getGameTemplates().getGameStartTime() );

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

	protected void setCompletedStatus()
	{
		try
		{
			setStatus( GameGlobals.COMPLETED );
			if( currTask != null )
				currTask.cancel();
			if( tableTimer != null )
			{
				tableTimer.cancel();
				tableCompletedStatus = true;
			}
			trackTableStatus();
			ActiveBoards.removeTable( getTableId() );
			ActiveBoards.removeTable( getGameTemplates().getId(), getTableId() );

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void scheduleStartGame( long time )
	{
		log.info("scheduleStartGame {}" ,gameStartFlag.get());
		if( gameStartFlag.get() )
		{
			log.info( "TableId : " + getTableId() + " Game Already Started" );
			return;
		}
		gameStartFlag.set( true );
		setStatus( GameGlobals.STARTING );
		log.info( "TableId : " + getTableId() + " Starting Game in : " + time );
		try
		{
			gameStartTime = System.currentTimeMillis() + time;
			if( currTask != null )
				currTask.cancel();
			currTask = new TableTimerTask( TimeTaskTypes.GAME_START )
			{
				@Override
				public void run()
				{
					try
					{
						startGame();
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( currTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public void scheduleTask( TimerTask task, long millis )
	{
		try
		{
			if( tableTimer != null )
				tableTimer.schedule( task, millis );
		}
		catch( Exception ex )
		{
			log.debug( "Problem In scheduleTask.." + getTableId() + ": Status" + this.status + ":: Table Status" + tableCompletedStatus );
			ex.printStackTrace();
		}
	}

	protected boolean checkPlayersAtStartGame()
	{
		return getPlayingPlayerSize() + getSeatedPlayerSize() >= getGameTemplates().getMinPlayer();
	}

	protected void sendBoardStatusMsg( long playerId )
	{
		long time = 0;
		try
		{
			if( status == GameGlobals.STARTING )
			{
				time = getGameTemplates().getGameStartTime();
				log.info("Time Happened{}",time );
				if( currTask != null && currTask.getTaskType() == TimeTaskTypes.GAME_START && playerId > 0)
				{
					time = ( long ) currTask.scheduledExecutionTime() - System.currentTimeMillis();
					time = time > 0 ? time : 0;
				}
				log.info("Time left Happened{}",time );
			}
			else if( status == GameGlobals.REGISTERING && gameNo > 0 && rummyGame != null )
			{
				log.info( "skipping boardstatus(2) as the game is on TableId : " + getTableId() );
				return;
			}

			if( playerId > 0 )
			{
				BoardStatus boardStatus = new BoardStatus( getTableId(), status, ( int ) time );
				dispatcher.sendMessage( playerId, boardStatus );

			}
			else
			{
				BoardStatus boardStatus = new BoardStatus( getTableId(), status, ( int ) time );
				dispatcher.sendMessage( getAllplayer(), boardStatus );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public void setOrderedPlayers( List< Long > orderedPlayers )
	{
		this.orderedPlayers = new ArrayList< Long >( orderedPlayers );
	}

	public List< Long > getOrderedPlayers()
	{
		return orderedPlayers;
	}

	protected void setTossRequired()
	{

	}

	/**
	 * @return the dispatcher
	 */
	public MessageDispatcher getDispatcher()
	{
		return dispatcher;
	}

	public Timer getTimer()
	{
		return tableTimer;
	}

	public void setTableIdDealer( int tableIdDealer )
	{
		this.tableIdDealer.set( tableIdDealer );
	}

	public RummyGame getRummyGame()
	{
		return rummyGame;
	}

	public void setCurrTask( TableTimerTask currTask )
	{
		this.currTask = currTask;
	}

	public void setScoreWindow( ScoreUpdate scoreWindow )
	{
		this.scoreUpdate = scoreWindow;
	}

	protected void setGameEndStatus()
	{
		setStatus( GameGlobals.IN_BETWEEN_MATCHES );
	}

	private void calculateSeatBalance( long losingPlayerId, BigDecimal losingAmt )
	{
		CurrencyDetails currencyDetails = userCurrencyDetails.get( losingPlayerId );

		BigDecimal remainingAmt = BigDecimal.ZERO;
		BigDecimal nonWithdraw = currencyDetails.getNonWithdrawable().compareTo( BigDecimal.ZERO ) > 0 && currencyDetails.getNonWithdrawable().compareTo( losingAmt ) >= 0
				? currencyDetails.getNonWithdrawable().subtract( losingAmt )
				: BigDecimal.ZERO;
		if( nonWithdraw.compareTo( BigDecimal.ZERO ) > 0 )
		{
			currencyDetails.setNonWithdrawable( nonWithdraw );
		}
		else
		{
			remainingAmt = currencyDetails.getNonWithdrawable().compareTo( BigDecimal.ZERO ) > 0 ? losingAmt.subtract( currencyDetails.getNonWithdrawable() ) : BigDecimal.ZERO;
			currencyDetails.setNonWithdrawable( BigDecimal.ZERO );
		}
		BigDecimal withdraw = remainingAmt.compareTo( BigDecimal.ZERO ) > 0 && currencyDetails.getWithdrawable().compareTo( BigDecimal.ZERO ) > 0
				&& currencyDetails.getWithdrawable().compareTo( remainingAmt ) >= 0 ? remainingAmt : BigDecimal.ZERO;
		if( withdraw.compareTo( BigDecimal.ZERO ) > 0 )
		{
			currencyDetails.setWithdrawable( withdraw );
		}
		else
		{
			remainingAmt = remainingAmt.compareTo( BigDecimal.ZERO ) > 0 && currencyDetails.getWithdrawable().compareTo( BigDecimal.ZERO ) > 0
					? remainingAmt.subtract( currencyDetails.getWithdrawable() )
					: BigDecimal.ZERO;
			currencyDetails.setWithdrawable( BigDecimal.ZERO );
		}
		BigDecimal deposit = remainingAmt.compareTo( BigDecimal.ZERO ) > 0 && currencyDetails.getDepositBucket().compareTo( BigDecimal.ZERO ) > 0
				&& currencyDetails.getDepositBucket().compareTo( remainingAmt ) >= 0 ? remainingAmt : BigDecimal.ZERO;
		if( deposit.compareTo( BigDecimal.ZERO ) > 0 )
		{
			currencyDetails.setDepositBucket( deposit );
		}
		else
		{
			remainingAmt = remainingAmt.compareTo( BigDecimal.ZERO ) > 0 && currencyDetails.getDepositBucket().compareTo( BigDecimal.ZERO ) > 0
					? remainingAmt.subtract( currencyDetails.getDepositBucket() )
					: BigDecimal.ZERO;
			currencyDetails.setDepositBucket( BigDecimal.ZERO );
		}
		userCurrencyDetails.put( losingPlayerId, currencyDetails );

	}

	protected long getWinner()
	{
		return winnerId;
	}

	protected void trackTableStatus()
	{
		TableStatusInfo statusInfo = TableStatusInfo.builder().templateId( getGameTemplates().getId() ).tableId( getTableId() ).maxPlayer( getGameTemplates().getMaxPlayer() ).availableSeats( getAvlSeat() )
				.status( getStatus() ).build();
		dispatcher.publishTableStatus( statusInfo );

	}
	
	

}
