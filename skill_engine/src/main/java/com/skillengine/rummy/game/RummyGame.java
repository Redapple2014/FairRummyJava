package com.skillengine.rummy.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.skillengine.rummy.cards.CardId;
import com.skillengine.rummy.globals.PlayerKickOutState;
import com.skillengine.rummy.globals.ResultantGameTypes;
import com.skillengine.rummy.globals.TimeTaskTypes;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.message.DealWinner;
import com.skillengine.rummy.message.Declare;
import com.skillengine.rummy.message.DeclareEvent;
import com.skillengine.rummy.message.DeclareEventTimeOut;
import com.skillengine.rummy.message.DisCardOutBound;
import com.skillengine.rummy.message.Drop;
import com.skillengine.rummy.message.DropResponse;
import com.skillengine.rummy.message.Finish;
import com.skillengine.rummy.message.FinishInited;
import com.skillengine.rummy.message.FinishPlayerTimeOut;
import com.skillengine.rummy.message.GameSetup;
import com.skillengine.rummy.message.GameStatus;
import com.skillengine.rummy.message.GameToss;
import com.skillengine.rummy.message.HandDiscardInfo;
import com.skillengine.rummy.message.Message;
import com.skillengine.rummy.message.PickCloseDeckOutBound;
import com.skillengine.rummy.message.PickClosedDeck;
import com.skillengine.rummy.message.PickOpenDeck;
import com.skillengine.rummy.message.PickOpenDeckOutBound;
import com.skillengine.rummy.message.PlayerBootedOut;
import com.skillengine.rummy.message.PlayerDeclaringDetails;
import com.skillengine.rummy.message.PlayerDeclaringState;
import com.skillengine.rummy.message.PlayerSequence;
import com.skillengine.rummy.message.PlayerTurnIntimation;
import com.skillengine.rummy.message.PlayerTurnTimeOut;
import com.skillengine.rummy.message.ResetCards;
import com.skillengine.rummy.message.ReshuffleCards;
import com.skillengine.rummy.message.ScoreUpdate;
import com.skillengine.rummy.message.TossInfo;
import com.skillengine.rummy.message.UserScore;
import com.skillengine.rummy.message.WrongFinishInfo;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.score.RummyGameScore;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.table.TableTimerTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RummyGame extends TrickTakingGame
{
	protected RummyBoard table;
	protected Object gamePlayLock = new Object();
	protected boolean tossRequired;
	private int totalPlayer = 0;
	private long gameStartTime;
	private HandModel handModel;
	protected long taskExecTime;
	private long gameDeclareTime = 0l;
	protected Map< Long, UserHandInfo > gamePlayerMap = new ConcurrentHashMap<>();
	protected AtomicBoolean endGame = new AtomicBoolean( false );
	protected long dealer;
	protected TimerTask handTask;
	protected Map< Long, Integer > playerScoreMap = new ConcurrentHashMap< Long, Integer >();
	private long winner;
	protected AtomicBoolean playerPlayed = new AtomicBoolean( false );
	protected AtomicLong finishPlayer = new AtomicLong( 0 );
	protected Map< Long, List< List< String > > > declaredCards = new ConcurrentHashMap<>();

	public RummyGame( RummyBoard table, List< PlayerInfo > playerIdList, boolean tossRequired, List< Long > dealsBootedOut )
	{
		this.table = table;
		this.tossRequired = tossRequired;
		this.totalPlayer = playerIdList.size();
		this.gameStartTime = System.currentTimeMillis();
		handModel = new HandModel( playerIdList, 2, table.getGameTemplates().getNoOfDeck(), table.getGameTemplates().getCardsPerPlayer() );
		for( int i = 0; i < totalPlayer; i++ )
		{
			PlayerInfo playerInfo = ( PlayerInfo ) playerIdList.get( i );
			UserHandInfo handInfo = UserHandInfo.builder().userId( playerInfo.getUserId() ).avatarId( playerInfo.getAvatarId() ).depositBalance( playerInfo.getDepositBalance() )
					.withdrawable( playerInfo.getWithdrawable() ).nonWithdrawable( playerInfo.getNonWithdrawable() ).build();
			addOrderedPlayerIds( playerInfo.getUserId() );
			gamePlayerMap.put( playerInfo.getUserId(), handInfo );
		}
		for( Long plId : dealsBootedOut )
		{
			setKnockedPlayer( plId, 0 );
		}

	}

	public long getGameStartTime()
	{
		return gameStartTime;
	}

	@Override
	public void scheduleMoveTimeOut( long time )
	{
		try
		{
			taskExecTime = System.currentTimeMillis() + time;
			if( handTask != null )
				handTask.cancel();
			handTask = new TimerTask()
			{
				public void run()
				{
					moveTimeOut();
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	@Override
	public void moveTimeOut()
	{
		try
		{
			changePlayMove();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void startCallback()
	{
		if( endGame.get() )
		{
			log.info( "TableId : " + table.getTableId() + " Game already ended" );
			return;
		}
		if( !tossRequired )
		{
			setState( GameStateChanges.DEALING );
			dealer = getOrderedPlayerIds().get( 0 );
			sendGameSetup();
			scheduleDealing( HandConfigs.DEAL_TIMEOUT );
		}
		else
		{
			setState( GameStateChanges.TOSS );
			sendTossMsg();
			scheduleTask();
		}
	}

	public void setState( int status )
	{
		try
		{
			setGameState( status );
			sendGameStatus( status );
			Thread.sleep( 200 );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	private void sendGameStatus( int status )
	{
		GameStatus st = new GameStatus( table.getTableId(), status, ( int ) ( taskExecTime - System.currentTimeMillis() ) );
		table.getDispatcher().sendMessage( table.getAllplayer(), st );
	}

	private void scheduleDealing( long time )
	{
		try
		{
			taskExecTime = System.currentTimeMillis() + time;
			if( handTask != null )
				handTask.cancel();
			handTask = new TimerTask()
			{
				public void run()
				{
					try
					{
						startGamePlay();
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public List< Message > sendReconDealMsg( long playerid, boolean exception )
	{
		long currentPlayer = -1;
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		List< Message > msgList = new ArrayList< Message >();
		boolean succ = false;
		try
		{
			if( getGameState() == GameStateChanges.COMPLETED )
				return null;
			List< List< String > > playerCardString = null;
			if( ( !getKnockedOutPlayer().containsKey( playerid ) || getKnockedOutPlayer().get( playerid ) == PlayerKickOutState.PLAYER_DROP
					|| getKnockedOutPlayer().get( playerid ) == PlayerKickOutState.WRONG_FINISH ) && handModel.getPlayersDeck( playerid ) != null )
			{
				UserHandInfo gamePlayer = gamePlayerMap.get( playerid );
				if( gamePlayer.getGroupCards() != null )
				{
					playerCardString = handModel.getPlayerGroupedCardStr( playerid, gamePlayer.getGroupCards() );
				}
				if( playerCardString == null )
				{
					playerCardString = handModel.getPlayerHandString( playerid );
				}
			}
			PlayerSequence playerSeq = null;
			if( getGameState() >= GameStateChanges.SEAT_SHUFFLE )
			{
				playerSeq = new PlayerSequence( table.getTableId(), getOrderedPlayerIds() );
				msgList.add( playerSeq );
			}
			GameSetup setup = null;
			if( getGameState() >= GameStateChanges.DEALING )
			{
				// Game Setup
				float closedDeckSize = ( float ) handModel.getClosedDeckSize() / ( table.getGameTemplates().getNoOfDeck() * 52 ) * 100;
				GameSetup gameSetup = new GameSetup( table.getTableId() );
				gameSetup.setHandCard( playerCardString );
				gameSetup.setClosedDeckSize( closedDeckSize );
				gameSetup.setDealerId( dealer );
				gameSetup.setJokerCard( handModel.getJokerCard().toString() );
				gameSetup.setOpenDeck( handModel.getOpenStackString() );
				gameSetup.setGameId( getGameId() );
				gameSetup.setCurrentDealNo( table.getCurrentGameNo() );
				msgList.add( gameSetup );
			}
			// Game Status
			GameStatus st = new GameStatus( table.getTableId(), getGameState(), ( int ) ( taskExecTime - System.currentTimeMillis() ) );
			msgList.add( st );
			if( exception )
			{
				msgList.add( st );
				if( playerSeq != null )
				{
					msgList.add( playerSeq );
				}
			}
			UserHandInfo player = null;
			for( long playerInGame : getOrderedPlayerIds() )
			{
				int timeLeft = 0;
				player = gamePlayerMap.get( playerInGame );
				if( player == null )
				{
					continue;
				}

				if( getKnockedOutPlayer().containsKey( playerInGame ) )
				{
					int knockedOutState = getKnockedOutPlayer().get( playerInGame );
					if( playerInGame == playerid )
					{
						if( knockedOutState == PlayerKickOutState.PLAYER_LEFT_TABLE || knockedOutState == PlayerKickOutState.BOOTED_OUT )
						{
							PlayerBootedOut bootedOut = new PlayerBootedOut( table.getTableId(), playerInGame, playerScoreMap.getOrDefault( playerInGame, 0 ),
									PlayerKickOutState.PLAYER_LEFT_TABLE );
							msgList.add( bootedOut );
							log.info( "TableId : " + table.getTableId() + "Player : " + playerid + " already left table(recon)" );
						}
					}
					if( knockedOutState == PlayerKickOutState.PLAYER_DROP )
					{
						DropResponse dropResponse = new DropResponse( table.getTableId(), playerInGame );
						msgList.add( dropResponse );
					}
					else if( knockedOutState == PlayerKickOutState.WRONG_FINISH )
					{
						WrongFinishInfo finishInfo = new WrongFinishInfo( table.getTableId(), playerInGame, null );
						msgList.add( finishInfo );
					}
				}
				else if( currentPlayer == playerInGame && !endGame.get() )
				{
					int time = getDeclareTime();

					if( handTask != null )
					{
						timeLeft = ( int ) ( handTask.scheduledExecutionTime() - System.currentTimeMillis() );
					}
					if( finishPlayer.get() == playerInGame )
					{
						timeLeft = ( int ) ( player.getDeclareStartTime() + time - System.currentTimeMillis() );
						FinishInited finishInited = new FinishInited( playerInGame, null, timeLeft );
						msgList.add( finishInited );

					}
					else if( finishPlayer.get() == 0 )
					{
						PlayerTurnIntimation intimation = new PlayerTurnIntimation( table.getTableId(), playerInGame, timeLeft, table.getGameTemplates().getGraceTime() );
						msgList.add( intimation );
					}

				}

			}
			log.info( "TableId : " + table.getTableId() + "Player : " + playerid + " Player state: " + getKnockedOutPlayer().get( playerid ) );
			if( endGame.get() )
			{
				List< Long > playingPlayers = new ArrayList< Long >( getOrderedPlayerIds() );
				playingPlayers.removeAll( playerScoreMap.keySet() );
				if( playingPlayers.size() > 0 )
				{
					int timeLeft = ( int ) ( handTask.scheduledExecutionTime() - System.currentTimeMillis() );
					if( playerScoreMap.containsKey( playerid ) || !getOrderedPlayerIds().contains( playerid ) )
					{
						DeclareEventTimeOut declareEventTimeOut = new DeclareEventTimeOut( table.getTableId(), -1 );
						msgList.add( declareEventTimeOut );
					}
					else
					{
						DealWinner dealWinner = new DealWinner( table.getTableId(), getWinner(), 0.0, true, getCustomGroupedCards( getWinner() ), table.getCurrentGameNo() );
						msgList.add( dealWinner );
						DeclareEvent declareEvent = new DeclareEvent( table.getTableId(), getDeclareTime() );
						msgList.add( declareEvent );
					}
				}
			}
			PlayerDeclaringDetails playerStateDetails = getPlayerStateDetails( new ArrayList< Long >( getOrderedPlayerIds() ), Collections.emptyList(), false );
			if( playerStateDetails != null )
			{
				msgList.add( playerStateDetails );

			}
			succ = true;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return msgList;
	}

	public boolean processUserLeft( long playerId )
	{
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " processUserLeft" );
		long currentPlayer = -1;
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer == playerId && getGameState() >= GameStateChanges.IN_PROGRESS )
		{
			if( !playerPlayed.get() )
			{
				playerPlayed.set( true );
				if( handTask != null )
				{
					handTask.cancel();
				}
			}
			else
			{
				log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + "played his turn" );
			}
		}

		if( getKnockedOutPlayer().containsKey( playerId ) )
		{
			if( getKnockedOutPlayer().get( playerId ) == PlayerKickOutState.PLAYER_LEFT_TABLE )
			{
				log.info( "TableId : " + table.getTableId() + "Player : " + playerId + " already left table" );
				return false;
			}
		}
		setKnockedPlayer( playerId, PlayerKickOutState.PLAYER_LEFT_TABLE );
		if( playerScoreMap.containsKey( playerId ) )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " score already calculated" );

		}
		else
		{
			UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
			int score = 0;
			if( this.getGameState() == GameStateChanges.NEAR_COMPLETION )
			{
				score = calculateScore( playerId );
			}
			else
			{
				if( gamePlayer.getMoveCount() > 0 )
				{
					if( finishPlayer.get() != 0 )
					{

						score = calculateScore( playerId );
					}
					else
					{
						score = HandConfigs.MIDDLE_DROP_POINTS;
					}
				}
				else
				{
					score = HandConfigs.EARLY_DROP_POINTS;
				}
			}
			playerScoreMap.put( playerId, score );
		}
		List< Long > playerIdList = new ArrayList< Long >();
		playerIdList.add( playerId );
		sendPlayerState( playerIdList, Collections.emptyList(), false );
		PlayerBootedOut bootedOut = new PlayerBootedOut( table.getTableId(), playerId, 0, PlayerKickOutState.PLAYER_LEFT_TABLE );
		table.getDispatcher().sendMessage( table.getAllplayer(), bootedOut );
		if( endGame.get() )
		{

			if( getPlayerScoreSize() == getPlayerSize() )
			{
				if( this.getGameState() != GameStateChanges.COMPLETED )
				{
					if( gameDeclareTime > 0 )
					{
						if( handTask != null )
							handTask.cancel();
						dealEnd();
					}
					else if( getKnockedPlayerSize() == getPlayerSize() )
					{
						boolean everyBodyLeft = true;
						Iterator< Long > knockedoutItr = getKnockedOutPlayer().keySet().iterator();
						while( knockedoutItr.hasNext() )
						{
							int knockedoutState = getKnockedOutPlayer().get( knockedoutItr.next() );
							if( knockedoutState != PlayerKickOutState.PLAYER_LEFT_TABLE )
							{
								everyBodyLeft = false;
								break;
							}
						}
						if( everyBodyLeft )
						{
							log.info( "TableId : " + table.getTableId() + " AllLeft " );
							setGameState( GameStateChanges.COMPLETED );
							scheduleEndTimeout( 500 );
						}
					}
				}
			}

			return true;
		}
		if( currentPlayer == playerId && getGameState() >= GameStateChanges.IN_PROGRESS )
		{
			scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );
		}
		else
		{

			if( getKnockedPlayerSize() + 1 >= getPlayerSize() )
			{
				ArrayList< Long > orderArrayList = new ArrayList< Long >( getOrderedPlayerIds() );
				orderArrayList.removeAll( getKnockedOutPlayer().keySet() );
				log.info( "TableId : " + table.getTableId() + "ALL : " + getOrderedPlayerIds() + " getKnockedOut : " + getKnockedOutPlayer() + " from processUserLeft" );
				if( orderArrayList.size() == 1 )
				{
					if( handTask != null )
						handTask.cancel();

					long activePlayer = orderArrayList.get( 0 );
					if( !playerScoreMap.containsKey( activePlayer ) )
					{
						playerScoreMap.put( activePlayer, 0 );
						setWinner( activePlayer );
						DealWinner dealWinner = new DealWinner( table.getTableId(), currentPlayer, 0.0, false, getCustomGroupedCards( getWinner() ), table.getCurrentGameNo() );
						table.getDispatcher().sendMessage( table.getAllplayer(), dealWinner );
						scheduleWinningTimeout( activePlayer, HandConfigs.FINISH_PLAYER_WINNING_TIMEOUT );
					}
					else
					{
						log.info( "TableId : " + table.getTableId() + " No Winner" );
						scheduleEndTimeout( 2 );
					}
				}
				else
				{
					log.error( "No Winner TableId : " + table.getTableId() );
					UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
					if( gamePlayer != null && gamePlayer.getTurnCount() == 0 )
					{
						log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Winner as TurnCnt = 0" );
						setWinner( playerId );
						playerScoreMap.remove( playerId );
					}
					dealEnd();
				}
			}
		}

		return true;
	}

	protected void scheduleTask( TimerTask task, long millis )
	{
		try
		{
			if( table.getTimer() != null )
				table.getTimer().schedule( task, millis );
		}
		catch( Exception ex )
		{
			log.error( "TableId : " + table.getTableId() + " Timer got cancelled", ex );
		}
	}

	private void sendGameSetup()
	{
		List< Long > playerSeq = getOrderedPlayerIds();
		if( !tossRequired )
		{
			playerSeq.remove( dealer );
			playerSeq.add( dealer );
			table.setOrderedPlayers( playerSeq );
		}

		log.info( "TableId : " + table.getTableId() + " Sending GameSetup.Dealer : " + dealer + " PlayerSeq : " + playerSeq + " Joker : " + handModel.getJokerCard() );
		float closedDeckSize = ( float ) handModel.getClosedDeckSize() / ( table.getGameTemplates().getNoOfDeck() * 52 ) * 100;
		for( long playerId : playerSeq )
		{
			List< List< String > > cardStr = handModel.getPlayerHandString( playerId );
			GameSetup gameSetup = new GameSetup( table.getTableId() );
			gameSetup.setHandCard( cardStr );
			gameSetup.setClosedDeckSize( closedDeckSize );
			gameSetup.setDealerId( dealer );
			gameSetup.setJokerCard( handModel.getJokerCard().toString() );
			gameSetup.setOpenDeck( handModel.getOpenStackString() );
			gameSetup.setGameId( getGameId() );
			gameSetup.setCurrentDealNo( table.getCurrentGameNo() );
			gameSetup.setTieBreakerStatus( table.isTiebreaker() ? 1 : 0 );
			table.getDispatcher().sendMessage( playerId, gameSetup );

		}

		List< Long > allPlayers = new ArrayList< Long >( table.getAllplayer() );
		allPlayers.removeAll( playerSeq );
		if( allPlayers.size() > 0 )
		{
			for( long nonPlayingPlayers : allPlayers )
			{
				GameSetup gameSetup = new GameSetup( table.getTableId() );
				gameSetup.setHandCard( null );
				gameSetup.setClosedDeckSize( closedDeckSize );
				gameSetup.setDealerId( dealer );
				gameSetup.setJokerCard( handModel.getJokerCard().toString() );
				gameSetup.setOpenDeck( handModel.getOpenStackString() );
				gameSetup.setGameId( getGameId() );
				gameSetup.setCurrentDealNo( table.getCurrentGameNo() );
				gameSetup.setTieBreakerStatus( table.isTiebreaker() ? 1 : 0 );
				table.getDispatcher().sendMessage( nonPlayingPlayers, gameSetup );
			}
		}
	}

	protected void sendTossMsg()
	{
		List< Long > playerIdList = new ArrayList< Long >( gamePlayerMap.keySet() );
		List< GameToss > tossCardList = new ArrayList< GameToss >();
		List< Integer > randomList = new ArrayList< Integer >();
		Random randomGenerator = new Random();
		int highCard = 0;
		while( playerIdList.size() > randomList.size() )
		{
			int randomInt = 4 + randomGenerator.nextInt( 52 );
			if( !randomList.contains( randomInt ) )
			{
				randomList.add( randomInt );
			}
			if( highCard < randomInt )
			{
				highCard = randomInt;
			}

		}

		long tossWinner = 0;
		final Map< Long, Integer > playerTossCardMap = new HashMap< Long, Integer >();
		for( int i = 0, size = playerIdList.size(); i < size; i++ )
		{
			GameToss tossCardObj = new GameToss();
			long playerId = playerIdList.get( i );
			int cardInt = randomList.get( i );
			int rank = cardInt / 4 + 1;
			int suite = cardInt % 4 + 1;
			CardId card = new CardId( suite, rank );
			tossCardObj.setGamePlayerID( playerId );
			tossCardObj.setCardId( card.toString() );
			tossCardList.add( tossCardObj );
			if( highCard == cardInt )
			{
				tossWinner = playerId;
			}
			playerTossCardMap.put( playerId, cardInt );
		}
		Collections.sort( playerIdList, new Comparator< Long >()
		{

			@Override
			public int compare( Long o1, Long o2 )
			{
				return playerTossCardMap.get( o2 ) - playerTossCardMap.get( o1 );
			}
		} );

		getOrderedPlayerIds().clear();

		for( long playerId : playerIdList )
		{
			addOrderedPlayerIds( playerId );
		}
		log.info( "Players after toss : " + getOrderedPlayerIds() );
		TossInfo info = new TossInfo( table.getTableId(), tossCardList, tossWinner );
		table.getDispatcher().sendMessage( playerIdList, info );

	}

	private void scheduleTask()
	{
		log.info( "scheduleTask : " + getGameState() );
		try
		{
			if( handTask != null )
				handTask.cancel();
			long time = 0;
			switch( getGameState() )
			{
			case GameStateChanges.NOT_STARTED:
				time = 0;
				break;
			case GameStateChanges.TOSS:
				time = HandConfigs.TOSS_TIMEOUT;
				break;
			case GameStateChanges.SEAT_SHUFFLE:
				time = HandConfigs.SEAT_SHUFFLE_TIMEOUT;
				break;
			}
			handTask = new TimerTask()
			{
				public void run()
				{
					try
					{
						switch( getGameState() )
						{
						case GameStateChanges.NOT_STARTED:
							setState( GameStateChanges.TOSS );
							sendTossMsg();
							scheduleTask();
							break;
						case GameStateChanges.TOSS:
							setState( GameStateChanges.SEAT_SHUFFLE );
							sendPlayerSeq();
							scheduleTask();
							break;
						case GameStateChanges.SEAT_SHUFFLE:
							setState( GameStateChanges.DEALING );
							sendGameSetup();
							scheduleDealing( HandConfigs.DEAL_TIMEOUT );
							break;
						}
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void sendPlayerSeq()
	{
		PlayerSequence playerSequence = new PlayerSequence( table.getTableId(), getOrderedPlayerIds() );
		table.getDispatcher().sendMessage( getOrderedPlayerIds(), playerSequence );
		dealer = getlastPlayingPlayer();
		table.setOrderedPlayers( getOrderedPlayerIds() );
		table.setTableIdDealer( table.getSeat( dealer ).getId() );
	}

	private void startGamePlay()
	{

		synchronized( gamePlayLock )
		{
			setCurrentPlayer( 0 );
		}
		long currentPlayer = whoseMove();
		log.info( "TableId : " + table.getTableId() + " playerTurn : " + currentPlayer + " FirstTurn" );
		if( getKnockedPlayerSize() + 1 == getPlayerSize() )
		{
			playerScoreMap.put( currentPlayer, 0 );
			setWinner( currentPlayer );
			// NO SHOW WINNER CASE AT THE START OF THE GAME PLAY
			DealWinner dealWinner = new DealWinner( table.getTableId(), currentPlayer, 0.0, false, getCustomGroupedCards( getWinner() ), table.getCurrentGameNo() );
			table.getDispatcher().sendMessage( table.getAllplayer(), dealWinner );
			scheduleWinnerTimeout( currentPlayer, HandConfigs.FINISH_PLAYER_WINNING_TIMEOUT );
			return;
		}
		if( getKnockedOutPlayer().containsKey( currentPlayer ) )
		{
			synchronized( gamePlayLock )
			{
				currentPlayer = switchtTurn().longValue();
			}
		}
		setState( GameStateChanges.IN_PROGRESS );
		int time = getPlayerTurnTime() + table.getGameTemplates().getGraceTime();
		playerPlayed.set( false );
		UserHandInfo handInfo = gamePlayerMap.get( currentPlayer );
		handInfo.incrementTurnCount();
		PlayerTurnIntimation intimation = new PlayerTurnIntimation( table.getTableId(), currentPlayer, time, table.getGameTemplates().getGraceTime() );
		table.getDispatcher().sendMessage( table.getAllplayer(), intimation );
		scheduleGamePlayTimeout( currentPlayer, time );
	}

	protected int getPlayerTurnTime()
	{
		return table.getGameTemplates().getPlayerTurnTime();
	}

	public void setWinner( long winner )
	{
		this.winner = winner;
		setGameState( GameStateChanges.NEAR_COMPLETION );
		table.setScoreWindow( null );
	}

	public void scheduleGamePlayTimeout( final long playerId, long time )
	{
		try
		{
			taskExecTime = System.currentTimeMillis() + time;
			if( handTask != null )
				handTask.cancel();
			handTask = new TimerTask()
			{
				public void run()
				{
					log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " playTimeout" );
					try
					{
						gamePlayTimeout( playerId );
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	public void gamePlayTimeout( long playerId )
	{
		try
		{
			long currentPlayer = -1;
			synchronized( gamePlayLock )
			{
				currentPlayer = whoseMove();
			}
			if( currentPlayer != playerId )
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " GamePlayTimeout  is not this player" );
				return;
			}
			if( playerPlayed.get() )
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
				return;
			}
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " timedout" );
			playerPlayed.set( true );
			CardId lastPCard = handModel.moveMissed( currentPlayer );
			if( lastPCard != null )
			{
				handModel.discard( currentPlayer, lastPCard );
				HandDiscardInfo info = new HandDiscardInfo( table.getTableId(), currentPlayer, lastPCard.toString() );
				table.getDispatcher().sendMessage( table.getAllplayer(), info );
			}
			else
			{
				UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
				boolean bootout = checkForBootout( playerId );
				if( bootout )
				{
					int score = 0;
					if( gamePlayer.getMoveCount() > 0 )
					{
						score = HandConfigs.MIDDLE_DROP_POINTS;
					}
					else
					{
						score = HandConfigs.EARLY_DROP_POINTS;
					}
					playerScoreMap.put( playerId, score );
					setKnockedPlayer( playerId, KickedOutStates.BOOTED_OUT );
					PlayerBootedOut bootedOut = new PlayerBootedOut( table.getTableId(), playerId, score, PlayerKickOutState.PLAYER_DROP );
					table.getDispatcher().sendMessage( table.getAllplayer(), bootedOut );
					table.userLeft( playerId, 2 );

				}
				else
				{
					if( table.getGameTemplates().getVariantType() == VariantTypes.DEALS_RUMMY )
					{
						int score = 0;
						if( gamePlayer.getMoveCount() > 0 )
						{
							score = HandConfigs.MIDDLE_DROP_POINTS;
						}
						else
						{
							score = HandConfigs.EARLY_DROP_POINTS;
						}
						playerScoreMap.put( playerId, score );
						setKnockedPlayer( playerId, KickedOutStates.PLAYER_DROP );
						DropResponse dropResponse = new DropResponse( table.getTableId(), playerId );
						table.getDispatcher().sendMessage( table.getAllplayer(), dropResponse );
					}
					else
					{
						PlayerTurnTimeOut playerTurnTimeOut = new PlayerTurnTimeOut( table.getTableId(), currentPlayer );
						table.getDispatcher().sendMessage( table.getAllplayer(), playerTurnTimeOut );
					}
				}
			}
			scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );
		}

		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	private boolean checkForBootout( long playerId )
	{

		int bootoutTurn = HandConfigs.PLAYER_BOOTOUT_TURN;
		boolean flag = false;
		if( table.getGameTemplates().getVariantType() == VariantTypes.POINTS_RUMMY && handModel.getMissedMoveCount( playerId ) == bootoutTurn )
		{
			flag = true;
		}

		return flag;
	}

	private void changePlayMove()
	{
		log.info( "GameState " + this.getGameState() );
		if( this.getGameState() == GameStateChanges.COMPLETED )
		{
			log.info( "Game State Complted Returning {}", getGameId() );
			return;
		}
		try
		{

			log.info( "TableId : " + table.getTableId() + " Knocked player : " + getKnockedPlayerSize() + " PlayerSize : " + getPlayerSize() );
			ArrayList< Long > activePlayerList = new ArrayList< Long >( getOrderedPlayerIds() );
			activePlayerList.removeAll( getKnockedOutPlayer().keySet() );
			if( activePlayerList.size() == 1 )
			{
				long currentPlayer = activePlayerList.get( 0 );
				if( !playerScoreMap.containsKey( currentPlayer ) )
				{
					playerScoreMap.put( currentPlayer, 0 );
					setWinner( currentPlayer );
					DealWinner dealWinner = new DealWinner( table.getTableId(), currentPlayer, 0.0, false, getCustomGroupedCards( getWinner() ), table.getCurrentGameNo() );
					table.getDispatcher().sendMessage( table.getAllplayer(), dealWinner );
					scheduleWinningTimeout( currentPlayer, HandConfigs.FINISH_PLAYER_WINNING_TIMEOUT );
				}
				else
				{
					log.info( "TableId : " + table.getTableId() + " No Winner" );
					scheduleEndTimeout( 2 );
				}
				return;
			}
			boolean reshuffle = false;
			if( handModel.getClosedDeckSize() == 0 )
			{
				reshuffle = handModel.resuffle();
				if( reshuffle )
				{
					ReshuffleCards cards = new ReshuffleCards( table.getTableId() );
					table.getDispatcher().sendMessage( table.getAllplayer(), cards );
				}
				else
				{
					log.error( "TableId : " + table.getTableId() + " Reshuffle failed" );
				}
			}
			long currentPlayer = 0;
			synchronized( gamePlayLock )
			{
				currentPlayer = switchtTurn().longValue();
			}
			playerPlayed.set( false );
			int timeOut = getPlayerTurnTime() + table.getGameTemplates().getGraceTime();
			UserHandInfo rummyPlayer = gamePlayerMap.get( currentPlayer );
			rummyPlayer.incrementTurnCount();
			PlayerTurnIntimation playerTurnIntimation = new PlayerTurnIntimation( table.getTableId(), currentPlayer, timeOut, table.getGameTemplates().getGraceTime() );
			table.getDispatcher().sendMessage( table.getAllplayer(), playerTurnIntimation );
			scheduleGamePlayTimeout( currentPlayer, timeOut );

		}
		catch( Exception e )
		{
			log.error( "Error in changePlayMove", e );
		}
	}

	public boolean pickFromOpenDeck( PickOpenDeck pickOpenCard, long userId )
	{
		long playerId = userId;
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickO" );
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		long currentPlayer = 0;
		boolean result = false;
		if( endGame.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickO. Game already over" );
			return true;
		}
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer != playerId )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickO - whoseMove() is not this player" );
			return true;
		}

		if( playerPlayed.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
			return true;
		}

		try
		{
			if( handModel.alreadyPickedCard( playerId ) || finishPlayer.get() > 0 )
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Already picked card. GamePlayerHandCardsSize : "
						+ handModel.getPlayerHand( playerId ).size() );

				return true;
			}
			if( handModel.canPickOpenCard() )
			{
				CardId openCard = handModel.pickOpenCard( playerId );

				if( openCard != null )
				{
					gamePlayer.incrementMoveCount();
					PickOpenDeckOutBound outBound = new PickOpenDeckOutBound( table.getTableId(), openCard.toString(), false, playerId );
					table.getDispatcher().sendMessage( table.getAllplayer(), outBound );
					if( pickOpenCard.getGroupCards() != null && pickOpenCard.getGroupCards().size() > 0 )
					{
						groupCards( playerId, pickOpenCard.getGroupCards(), openCard );
					}
				}
				else
				{
					log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " openCard is NULL" );

				}
			}
			else
			{
				PickOpenDeckOutBound outBound = new PickOpenDeckOutBound( table.getTableId(), null, true, playerId );
				table.getDispatcher().sendMessage( table.getAllplayer(), outBound );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return true;
		}
		return result;
	}

	public void groupCards( long playerId, List< List< String > > groupCardsStr, CardId lastPickedOrDiscarded )
	{
		if( groupCardsStr == null )
		{
			return;
		}
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		try
		{
			if( !getKnockedOutPlayer().containsKey( playerId ) )
			{
				List< List< CardId > > groupCards = handModel.checkGroupedCardForSetCards( playerId, groupCardsStr, lastPickedOrDiscarded );
				if( groupCards != null )
				{
					gamePlayer.setGroupCards( groupCards );
				}
				else
				{
					log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Invalid cards in GroupCards from Discard/Finish : " + groupCardsStr
							+ " ActualCards : " + handModel.getPlayerHandString( playerId ) );
					sendHandCards( playerId );
				}
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			log.error( "Error in setGroupCards", ex );
		}
	}

	private void sendHandCards( long playerId )
	{
		List< List< String > > cardStr = handModel.getPlayerHandString( playerId );
		ResetCards cards = new ResetCards( table.getTableId(), cardStr );
		table.getDispatcher().sendMessage( playerId, cards );
	}

	public boolean pickFromClosedDeck( PickClosedDeck pickClosedCard, long userId )
	{
		long playerId = userId;
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickC" );
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		long currentPlayer = 0;
		boolean result = false;
		if( endGame.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickC. Game already over" );
			return true;
		}
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer != playerId )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " PickC - whoseMove() is not this player" );
			return true;
		}

		if( playerPlayed.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
			return true;
		}
		try
		{
			if( handModel.alreadyPickedCard( playerId ) || finishPlayer.get() > 0 )
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Already picked card. : " + handModel.getPlayerHand( playerId ).size() );

				return true;
			}
			CardId closedCard = handModel.pickClosedCard( playerId );
			if( closedCard != null )
			{
				gamePlayer.incrementMoveCount();
				PickCloseDeckOutBound outBound = new PickCloseDeckOutBound( table.getTableId(), playerId, closedCard.toString() );
				table.getDispatcher().sendMessage( table.getAllplayer(), outBound );
				if( pickClosedCard.getGroupCards() != null && pickClosedCard.getGroupCards().size() > 0 )
				{
					groupCards( playerId, pickClosedCard.getGroupCards(), closedCard );
				}
			}
			else
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " closedCard is NULL. : " + handModel.getPlayerHand( playerId ).size() );

			}
			return result;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return true;
		}
	}

	public boolean discard( com.skillengine.rummy.message.Discard discard, long userId )
	{
		boolean succ = false;
		long playerId = userId;
		long currentPlayer = 0;
		if( endGame.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Discard. Game already over" );
			return true;
		}
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Discard : " + discard.getDiscardCardId() );
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer != playerId )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Discard - whoseMove() is not this player" );
			return true;
		}
		if( playerPlayed.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
			return true;
		}
		try
		{
			CardId discardCard = new CardId( discard.getDiscardCardId() );
			succ = handModel.discard( playerId, discardCard );
			if( succ )
			{
				playerPlayed.set( true );
				if( handTask != null )
					handTask.cancel();
				DisCardOutBound outBound = new DisCardOutBound( table.getTableId(), playerId, discardCard.toString() );
				table.getDispatcher().sendMessage( table.getAllplayer(), outBound );
				scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );
			}
			else
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Discard is invalid" );
				return true;
			}
			return false;
		}
		catch( Exception ex )
		{
			log.error( "Error in Discard", ex );
			return true;
		}
	}

	private void scheduleWinnerTimeout( final long finishPlayer, long time )
	{
		log.info( "TableId : " + table.getTableId() + " scheduleWinnerTimeout" );
		endGame.set( true );
		try
		{
			TimerTask currTask = new TimerTask()
			{
				public void run()
				{
					List< Long > playingPlayers = new ArrayList< Long >( getOrderedPlayerIds() );
					playingPlayers.removeAll( playerScoreMap.keySet() );
					if( playingPlayers.size() > 0 )
					{
						for( long playingPlayer : playingPlayers )
						{
							DeclareEvent declareEvent = new DeclareEvent( table.getTableId(), getDeclareTime() );
							table.getDispatcher().sendMessage( playingPlayer, declareEvent );
						}
						DeclareEventTimeOut declareEventTimeOut = new DeclareEventTimeOut( table.getTableId(), -1 );
						table.getDispatcher().sendMessage( finishPlayer, declareEventTimeOut );
						Iterator< Long > droppedPlayers = playerScoreMap.keySet().iterator();
						while( droppedPlayers.hasNext() )
						{
							long droppedPlayer = droppedPlayers.next();
							DeclareEventTimeOut droppedPlayerTimeOut = new DeclareEventTimeOut( table.getTableId(), -1 );
							table.getDispatcher().sendMessage( droppedPlayer, droppedPlayerTimeOut );
						}

						scheduleDeclareTimeout( getDeclareTime() );
					}
					else
					{
						if( getGameState() != GameStateChanges.COMPLETED )
						{
							dealEnd();
						}
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

	protected int getDeclareTime()
	{
		return HandConfigs.DECLARE_TIMEOUT;
	}

	public void scheduleDeclareTimeout( long time )
	{
		try
		{
			taskExecTime = System.currentTimeMillis() + time;
			if( handTask != null )
				handTask.cancel();
			handTask = new TimerTask()
			{
				public void run()
				{
					log.info( "TableId : " + table.getTableId() + " declareTimeout" );
					try
					{
						playersDeclareTimeout();
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	private void playersDeclareTimeout()
	{
		log.info( "TableId : " + table.getTableId() + " playersDeclareTimeout" );
		if( this.getGameState() == GameStateChanges.COMPLETED )
		{
			return;
		}
		ArrayList< Long > notDelaredPlayers = new ArrayList< Long >( getOrderedPlayerIds() );
		notDelaredPlayers.removeAll( playerScoreMap.keySet() );
		for( long playerId : notDelaredPlayers )
		{
			UserHandInfo userHandInfo = gamePlayerMap.get( playerId );
			int score = calculateScore( playerId );
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Score in declareTimeout : " + score );
			playerScoreMap.put( playerId, score );

		}
		sendPlayerState( Collections.emptyList(), notDelaredPlayers, true );
		if( this.getGameState() != GameStateChanges.COMPLETED )
		{
			scheduleDealEnd( HandConfigs.DECLARE_END_TIMEOUT );
		}

	}

	private int calculateScore( long playerId )
	{
		boolean isFinishPlayer = false;
		if( finishPlayer.get() == playerId )
		{
			isFinishPlayer = true;
		}
		RummyGameScore scoreCalc = new RummyGameScore();
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		List< List< CardId > > groupCards = gamePlayer.getGroupCards();
		int grpCardSize = 0;
		if( groupCards != null )
		{
			for( List< CardId > cards : groupCards )
			{
				grpCardSize += cards.size();
			}

		}
		if( groupCards == null || grpCardSize != 13 )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Problem with Group Card During Scoring : " + groupCards );
			groupCards = handModel.getGamePlayerHandCards( playerId );

		}
		scoreCalc.setPlayerCard( groupCards );
		scoreCalc.setJokerCard( handModel.getJokerCard() );
		boolean playerWithNoMoveInDealShowGame = false;
		if( gamePlayer.getMoveCount() == 0 )
		{
			playerWithNoMoveInDealShowGame = true;
		}
		int score = scoreCalc.calculateScore( playerId, isFinishPlayer, HandConfigs.FULL_POINTS, playerWithNoMoveInDealShowGame );
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + "Scoring Cards : " + groupCards + " Score : " + score );
		return score;
	}

	private void scheduleDealEnd( long time )
	{
		log.info( "TableId : " + table.getTableId() + " scheduleDealEnd" );
		handTask = new TimerTask()
		{
			public void run()
			{
				try
				{
					dealEnd();
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		};
		scheduleTask( handTask, time );
	}

	protected void dealEnd()
	{
		log.info( "TableId : " + table.getTableId() + " dealEnd" );
		if( getGameState() == GameStateChanges.COMPLETED )
		{
			log.info( "TableId : " + table.getTableId() + " dealEnd  Game already completed" );
			return;
		}
		setGameState( GameStateChanges.COMPLETED );
		scheduleDealEndTimeout( HandConfigs.DEAL_END_TIMEOUT );

	}

	private void scheduleDealEndTimeout( final long time )
	{
		log.info( "TableId : " + table.getTableId() + " scheduleDealEndTimeout" );
		TimerTask currTask = new TimerTask()
		{
			public void run()
			{
				try
				{
					endGame();
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		};
		scheduleTask( currTask, time );
	}

	private void endGame()
	{
		log.info( "TableId : " + table.getTableId() + " endGame" );
		try
		{
			sendScores();
			long totalGameTime = System.currentTimeMillis() - gameStartTime;
			log.info( "TableId : " + table.getTableId() + " GameEndTime : " + new Date() + " Knockedout players : " + getKnockedOutPlayer() + " GameDuration : " + totalGameTime );
			end( new Date() );
			sendGameStatus( getGameState() );
			int endGameTimeout = HandConfigs.END_TIMEOUT;
			scheduleEndTimeout( endGameTimeout );
		}
		catch( Throwable e )
		{

			e.printStackTrace();
		}
	}

	private void scheduleEndTimeout( final long time )
	{
		TableTimerTask currTask = new TableTimerTask( TimeTaskTypes.GAME_END )
		{
			public void run()
			{
				try
				{
					table.endGame();
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		};
		table.setCurrTask( currTask );
		scheduleTask( currTask, time );
	}

	public void finish( Finish finish, long userId )
	{
		long playerId = userId;
		int time = getDeclareTime();
		UserHandInfo handInfo = gamePlayerMap.get( playerId );
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Finish : " + finish.getDiscardCardId() + " Move : " + handInfo.getMoveCount() + " GroupCards : "
				+ finish.getGroupCards() );
		long currentPlayer = 0;
		if( endGame.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Finish. Game already over" );
			return;
		}
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer != playerId )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Finish - whoseMove() is not this player" );
			return;
		}
		if( playerPlayed.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
			return;
		}
		boolean succ = false;
		try
		{
			CardId discardCard = new CardId( finish.getDiscardCardId() );
			succ = handModel.finish( playerId, discardCard );
			if( succ )
			{
				groupCards( playerId, finish.getGroupCards(), discardCard );
				playerPlayed.set( true );
				if( handTask != null )
					handTask.cancel();
				finishPlayer.set( playerId );
				handInfo.setDeclareStartTime( System.currentTimeMillis() );
				FinishInited finishInited = new FinishInited( table.getTableId(), playerId, discardCard.toString(), getDeclareTime() );
				table.getDispatcher().sendMessage( table.getAllplayer(), finishInited );
				declaredCards.put( playerId, finish.getGroupCards() );
				scheduleDeclareTimeout( playerId, time );
				log.info( "Finsih player set and FInish Broad cast sent " + finishPlayer.get() );
			}
			else
			{
				log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Finish Error" );
			}
		}
		catch( Exception ex )
		{
			log.error( "Error in Finish", ex );
			ex.printStackTrace();
		}
	}

	public void scheduleDeclareTimeout( final long playerId, long time )
	{
		try
		{
			taskExecTime = System.currentTimeMillis() + time;
			if( handTask != null )
				handTask.cancel();
			handTask = new TimerTask()
			{
				public void run()
				{
					log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " declareTimeout" );
					try
					{
						finishPlayerDeclareTimeout( playerId );
					}
					catch( Exception ex )
					{
						ex.printStackTrace();
					}
				}
			};
			scheduleTask( handTask, time );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void finishPlayerDeclareTimeout( long playerId )
	{
		int score = calculateScore( playerId );
		if( score > 0 )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " declareTimeout with wrong show" );
			setKnockedPlayer( playerId, PlayerKickOutState.WRONG_FINISH );
			finishPlayer.set( 0 );
			handModel.addFcardToOpenDeck();
			playerScoreMap.put( playerId, HandConfigs.FULL_POINTS );
			List< Message > msgs = new ArrayList<>();
			// WrongFinishInfo finishInfo = new WrongFinishInfo(
			// table.getTableId(), playerId,
			// handModel.getFinishedCard().toString() );
			// msgs.add( finishInfo );
			FinishPlayerTimeOut finishPlayerTimeOut = new FinishPlayerTimeOut( table.getTableId(), playerId, handModel.getFinishedCard().toString() );
			msgs.add( finishPlayerTimeOut );
			table.getDispatcher().sendMessage( table.getAllplayer(), msgs );
			List< Long > playerIdList = new ArrayList< Long >();
			playerIdList.add( playerId );
			sendPlayerState( playerIdList, Collections.emptyList(), false );
			scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );
		}
		else
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " declareTimeout with show" );
			setWinner( playerId );
			DealWinner dealWinner = new DealWinner( table.getTableId(), playerId, 0.0, true, getCustomGroupedCards( getWinner() ), table.getCurrentGameNo() );
			table.getDispatcher().sendMessage( table.getAllplayer(), dealWinner );
			scheduleWinningTimeout( playerId, HandConfigs.FINISH_PLAYER_WINNING_TIMEOUT );
		}

	}

	protected PlayerDeclaringDetails getPlayerStateDetails( List< Long > playingPlayersStatus, List< Long > notDeclared, boolean isDeclaringDone )
	{
		PlayerDeclaringDetails playerStateDetails = null;
		try
		{
			if( !playingPlayersStatus.isEmpty() )
			{
				List< PlayerDeclaringState > pStateList = new ArrayList<>();
				for( long pId : playingPlayersStatus )
				{
					int status = ResultantGameTypes.PLAYER_DECLARING;
					List< List< String > > grpCardIds = new ArrayList< List< String > >();
					if( pId == getWinner() )
					{
						status = ResultantGameTypes.WON;
						grpCardIds = declaredCards.get( pId );
					}
					if( declaredCards.get( pId ) != null && pId != getWinner() )
					{
						status = ResultantGameTypes.LOST;
						grpCardIds = declaredCards.get( pId );
					}
					if( isDeclaringDone && notDeclared.contains( pId ) )
					{
						status = ResultantGameTypes.LOST;
						grpCardIds = getCustomGroupedCards( pId );
					}
					int knockState = getKnockedOutPlayer().getOrDefault( pId, -1 );
					if( knockState > 0 )
					{
						status = ResultantGameTypes.PLAYER_DROP;
					}
					int score = playerScoreMap.getOrDefault( pId, 0 );
					pStateList.add( new PlayerDeclaringState( pId, grpCardIds, score, status ) );

				}
				if( !pStateList.isEmpty() )
				{
					playerStateDetails = new PlayerDeclaringDetails( table.getTableId(), winner, pStateList, table.getCurrentGameNo() );
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return playerStateDetails;
	}

	public List< List< String > > getDeclaredCards( int status, long playerId )
	{
		if( status == ResultantGameTypes.PLAYER_DROP )
		{
			return Collections.emptyList();
		}
		if( declaredCards.get( playerId ) != null )
		{
			return declaredCards.get( playerId );
		}
		return Collections.emptyList();
	}

	private int getGameResultStatus( long pId )
	{
		int status = ResultantGameTypes.LOST;
		if( getKnockedOutPlayer().containsKey( pId ) )
		{
			int knockoutState = getKnockedOutPlayer().get( pId );
			switch( knockoutState )
			{
			case PlayerKickOutState.GAME_PLAYER_TIMEOUT:
			case PlayerKickOutState.BOOTED_OUT:
			case PlayerKickOutState.PLAYER_DROP:
				status = ResultantGameTypes.PLAYER_DROP;
				break;
			case PlayerKickOutState.PLAYER_LEFT_TABLE:
				if( gamePlayerMap.containsKey( pId ) && gamePlayerMap.get( pId ).getDeclareStartTime() <= 0 )
				{
					status = ResultantGameTypes.PLAYER_DROP;
				}
				break;
			case PlayerKickOutState.WRONG_FINISH:
				status = ResultantGameTypes.PLAYER_DROP;
				break;
			default:
				status = ResultantGameTypes.PLAYER_DECLARING;
			}
		}
		return status;
	}

	private void scheduleWinningTimeout( final long finishPlayer, long time )
	{
		log.info( "TableId : " + table.getTableId() + " scheduleWinningTimeout" );
		endGame.set( true );
		try
		{
			TimerTask currTask = new TimerTask()
			{
				public void run()
				{
					List< Long > playingPlayers = new ArrayList< Long >( getOrderedPlayerIds() );
					playingPlayers.removeAll( playerScoreMap.keySet() );
					if( playingPlayers.size() > 0 )
					{
						for( long playingPlayer : playingPlayers )
						{
							DeclareEvent declareEvent = new DeclareEvent( table.getTableId(), getDeclareTime() );
							table.getDispatcher().sendMessage( playingPlayer, declareEvent );
							gamePlayerMap.get( playingPlayer ).setDeclareStartTime( System.currentTimeMillis() );
						}
						DeclareEventTimeOut declareEventTimeOut = new DeclareEventTimeOut( table.getTableId(), -1 );
						table.getDispatcher().sendMessage( finishPlayer, declareEventTimeOut );
						Iterator< Long > droppedPlayers = playerScoreMap.keySet().iterator();
						while( droppedPlayers.hasNext() )
						{
							long droppedPlayer = droppedPlayers.next();
							DeclareEventTimeOut droppedPlayerTimeOut = new DeclareEventTimeOut( table.getTableId(), -1 );
							table.getDispatcher().sendMessage( droppedPlayer, droppedPlayerTimeOut );
						}
						scheduleDeclareTimeout( getDeclareTime() );

					}
					else
					{
						if( getGameState() != GameStateChanges.COMPLETED )
						{
							dealEnd();
						}
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

	private void sendPlayerState( List< Long > playingPlayersStatus, List< Long > notDeclaredPlayers, boolean isDeclaringDone )
	{
		PlayerDeclaringDetails playerStateDetails = getPlayerStateDetails( getOrderedPlayerIds(), notDeclaredPlayers, isDeclaringDone );
		if( playerStateDetails != null )
		{
			table.getDispatcher().sendMessage( table.getAllplayer(), playerStateDetails );
		}
	}

	private void sendScores()
	{
		Iterator< Long > playerIdItr = playerScoreMap.keySet().iterator();
		List< UserScore > playerScoreList = new ArrayList<>();
		int winnerScore = 0;
		while( playerIdItr.hasNext() )
		{
			long declaredPlayer = playerIdItr.next();
			if( declaredPlayer == winner )
			{
				continue;
			}

			int score = playerScoreMap.get( declaredPlayer );
			int status = getGameResultStatus( declaredPlayer );
			UserScore userScore = getPlayerScoreObj( declaredPlayer, score, status );
			playerScoreList.add( userScore );
		}
		List< Long > declaringPlayers = new ArrayList< Long >( getOrderedPlayerIds() );
		declaringPlayers.removeAll( getKnockedOutPlayer().keySet() );
		declaringPlayers.removeAll( playerScoreMap.keySet() );
		for( long declaringPlayer : declaringPlayers )
		{
			UserScore playerScoreObj = getPlayerScoreObj( declaringPlayer, 0, ResultantGameTypes.PLAYER_DECLARING );
			playerScoreList.add( playerScoreObj );
		}
		if( winner > 0 )
		{
			UserScore playerScoreObj = getPlayerScoreObj( winner, winnerScore, ResultantGameTypes.WON );
			playerScoreList.add( playerScoreObj );
		}

		ScoreUpdate scoreBoard = new ScoreUpdate( table.getTableId(), handModel.getJokerCard().toString(), playerScoreList, table.getCurrentGameNo() );
		table.setScoreWindow( scoreBoard );
		table.getDispatcher().sendMessage( table.getAllplayer(), scoreBoard );
		log.error( "TableId : " + table.getTableId() + "   sendScoreBoard  : " + scoreBoard + "  all Players :" + table.getAllplayer() );

	}

	private UserScore getPlayerScoreObj( long playerId, int score, int status )
	{
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		List< List< String > > groupCardStr = null;
		int knockoutState = getKnockedOutPlayer().containsKey( playerId ) ? getKnockedOutPlayer().get( playerId ) : 0;
		boolean showCards = finishPlayer.get() <= 0 || knockoutState == PlayerKickOutState.BOOTED_OUT || knockoutState == PlayerKickOutState.PLAYER_DROP
				|| ( knockoutState == PlayerKickOutState.PLAYER_LEFT_TABLE && gamePlayer.getDeclareStartTime() <= 0 ) ? false : true;
		if( status != ResultantGameTypes.PLAYER_DECLARING && showCards )
		{
			if( gamePlayer.getGroupCards() != null )
			{
				groupCardStr = handModel.getPlayerGroupedCardStr( playerId, gamePlayer.getGroupCards() );
				if( groupCardStr == null )
				{
					log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " NULL GroupCards!!!. GroupCards : " + getCardsStr( gamePlayer.getGroupCards() )
							+ " HandCards : " + handModel.getPlayerHand( playerId ) );
				}
			}
		}
		else
		{
			List< List< CardId > > sortedCards = handModel.sortGamePlayerHandCardss( playerId );
			gamePlayer.setGroupCards( sortedCards );
			groupCardStr = handModel.getPlayerGroupedCardStr( playerId, sortedCards );
			if( groupCardStr == null )
			{
				log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " NULL GroupCards!!!. GroupCards : " + getCardsStr( sortedCards ) + " HandCards : "
						+ handModel.getPlayerHand( playerId ) );

			}
		}
		int userScore = playerScoreMap.get( playerId );
		int pointValue = table.getGameTemplates().getPointValue();
		if( playerId != winner )
		{
			userScore = -( userScore * pointValue );
		}
		UserScore userScoreMsg = new UserScore( playerId, userScore, getCustomGroupedCards( playerId ), status, table.getGameTemplates().getPointValue() );
		return userScoreMsg;
	}

	public List< List< String > > getCardsStr( final List< List< CardId > > rummyCards )
	{
		List< List< String > > rummyCardsStr = new ArrayList< List< String > >();
		for( List< CardId > cardIdList : rummyCards )
		{
			List< String > cardIdStrList = new ArrayList< String >();
			for( CardId groupCard : cardIdList )
			{
				cardIdStrList.add( groupCard.toString() );

			}
			rummyCardsStr.add( cardIdStrList );
		}
		return rummyCardsStr;
	}

	public void drop( Drop drop, long userId )
	{
		long playerId = userId;
		UserHandInfo userHandInfo = gamePlayerMap.get( playerId );
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Drop .MoveCnt : " + userHandInfo.getMoveCount() );
		long currentPlayer = 0;
		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}
		if( currentPlayer != playerId )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Drop - whoseMove() is not this player" );
			return;
		}
		if( playerPlayed.get() )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " already played" );
			return;
		}
		playerPlayed.set( true );
		if( handTask != null )
			handTask.cancel();
		int score = 0;
		if( userHandInfo.getMoveCount() > 0 )
		{
			score = HandConfigs.MIDDLE_DROP_POINTS;

		}
		else
		{
			score = HandConfigs.EARLY_DROP_POINTS;

		}
		playerScoreMap.put( playerId, score );
		getKnockedOutPlayer().put( playerId, PlayerKickOutState.PLAYER_DROP );
		DropResponse dropResponse = new DropResponse( table.getTableId(), playerId );
		table.getDispatcher().sendMessage( table.getAllplayer(), dropResponse );
		// table.changePlayerBalance( playerId, chipValue,
		// GameTableConstants.TxnStatus.LOST,
		// GameTableConstants.TxnType.DEBIT );
		// changePlayMove();
		scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );

	}

	public long getWinner()
	{
		return winner;
	}

	public void declare( Declare declare, long userId )
	{

		long playerId = userId;
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Declare : " + declare.getCardsGrouping() );
		UserHandInfo gamePlayer = gamePlayerMap.get( playerId );
		long currentPlayer = 0;

		if( playerScoreMap.containsKey( playerId ) )
		{
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " score already calculated" );
			return;
		}

		synchronized( gamePlayLock )
		{
			currentPlayer = whoseMove();
		}

		boolean finishPlayerDeclare = false;
		// It is a finish player declare
		if( finishPlayer.get() == playerId )
		{
			finishPlayerDeclare = true;
			if( currentPlayer != playerId )
			{
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Finish Declare - whoseMove() is not this player" );
				return;
			}
			if( handTask != null )
				handTask.cancel();

		}
		List< List< String > > groupCardsStr = declare.getCardsGrouping();
		List< List< CardId > > groupCards = handModel.checkGroupedCard( playerId, groupCardsStr );
		if( groupCards != null )
		{
			gamePlayer.setGroupCards( groupCards );
		}
		else
		{
			log.error( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Invalid cards in GroupCards from Declare : " + groupCardsStr + " ActualCards : "
					+ handModel.getPlayerHandString( playerId ) );
		}
		declaredCards.put( playerId, groupCardsStr );
		int score = calculateScore( playerId );
		log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " Score : " + score + " RummyCards : " + gamePlayer.getGroupCards() + " Finish Player : "
				+ finishPlayerDeclare );
		if( finishPlayerDeclare )
		{

			if( score > 0 )
			{
				setKnockedPlayer( playerId, PlayerKickOutState.WRONG_FINISH );
				finishPlayer.set( 0 );
				handModel.removeFinishMatchPlayerId();
				handModel.addFcardToOpenDeck();
				playerScoreMap.put( playerId, HandConfigs.FULL_POINTS );
				WrongFinishInfo wrongFinishInfo = new WrongFinishInfo( table.getTableId(), playerId, handModel.getFinishedCard().toString() );
				table.getDispatcher().sendMessage( table.getAllplayer(), wrongFinishInfo );
				List< Long > playerIdList = new ArrayList< Long >();
				playerIdList.add( playerId );
				ArrayList< Long > activePlayerList = new ArrayList< Long >( getOrderedPlayerIds() );
				activePlayerList.removeAll( getKnockedOutPlayer().keySet() );
				if( activePlayerList.size() == 1 )
				{
					changePlayMove();
				}
				else
				{

					scheduleMoveTimeOut( HandConfigs.MOVE_TIMEOUT );
				}
				return;
			}
			else
			{
				setWinner( playerId );
				gameDeclareTime = System.currentTimeMillis();
				playerScoreMap.put( playerId, score );
				List< Message > messages = new ArrayList<>();
				DealWinner dealWinner = new DealWinner( table.getTableId(), playerId, 0.0, true, getCustomGroupedCards( playerId ), table.getCurrentGameNo() );
				messages.add( dealWinner );
				table.getDispatcher().sendMessage( table.getAllplayer(), messages );
				scheduleWinningTimeout( playerId, HandConfigs.FINISH_PLAYER_WINNING_TIMEOUT );
				log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + " finish with show" );
				Set< Long > playerIds = playerScoreMap.keySet();
				int winScore = 0;
				for( Long plyrId : playerIds )
				{
					if( plyrId != playerId )
					{
						winScore = winScore + playerScoreMap.get( plyrId );
					}
				}
			}
		}
		else
		{
			playerScoreMap.put( playerId, score == 0 ? 2 : score );
			log.info( "TableId : " + table.getTableId() + " PlayerId : " + playerId + "Game.. Looser" );
			List< Long > playerIdList = new ArrayList< Long >();
			playerIdList.add( playerId );
			sendPlayerState( playerIdList, Collections.emptyList(), false );

		}
		if( getPlayerScoreSize() == getPlayerSize() )
		{
			if( handTask != null )
				handTask.cancel();
			if( this.getGameState() != GameStateChanges.COMPLETED )
			{
				scheduleDealEnd( HandConfigs.DECLARE_END_TIMEOUT );
			}
		}
		else if( !finishPlayerDeclare )
		{
			int timeLeft = ( int ) ( handTask.scheduledExecutionTime() - System.currentTimeMillis() );
			DeclareEventTimeOut declareEventTimeOut = new DeclareEventTimeOut( table.getTableId(), timeLeft );
			table.getDispatcher().sendMessage( playerId, declareEventTimeOut );
		}
	}

	public int getPlayerScoreSize()
	{
		return playerScoreMap.size();
	}

	public Map< Long, UserHandInfo > getUserHandInfo()
	{

		return gamePlayerMap;
	}

	public Map< Long, Integer > getScoreMap()
	{
		return playerScoreMap;
	}

	public List< String > getDiscardCards()
	{
		return handModel.getDiscardCards();
	}

	public void addHandCard( long userId, List< List< String > > handCards )
	{
		handModel.addGroupedHandCard( userId, handCards );
	}

	public List< List< String > > getCustomGroupedCards( long userId )
	{
		if( declaredCards.get( userId ) != null )
		{
			return declaredCards.get( userId );
		}
		if( handModel.isGroupCardAvail( userId ) )
		{
			return handModel.getGroupHandCard( userId );
		}
		List< List< String > > playerHandCard = handModel.getPlayerHandString( userId );
		if( playerHandCard == null )
		{
			return Collections.emptyList();
		}
		return playerHandCard;
	}

}
