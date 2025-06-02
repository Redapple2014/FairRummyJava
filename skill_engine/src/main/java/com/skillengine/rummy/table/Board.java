/**
 * 
 */
package com.skillengine.rummy.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.skillengine.common.GameTemplates;
import com.skillengine.rummy.globals.PlayerGlobals;
import com.skillengine.rummy.globals.SeatsGlobals;
import com.skillengine.rummy.player.PlayerInfo;
import com.skillengine.rummy.player.SeatPlayerInfo;
import com.skillengine.sessions.PlayerSession;

/**
 * 
 */
public abstract class Board
{

	protected List< PlayerInfo > observers;
	protected List< PlayerInfo > curPlayingPlayer;
	private Map< Long, PlayerInfo > playingPlayerMap;
	private long tableId;
	protected int status;
	protected int gameType;
	protected boolean isLastGame;
	protected List< SeatInfo > seats;
	private GameTemplates gameTemplates;

	public Board( long tableId, GameTemplates templateDetails )
	{
		try
		{
			this.gameType = templateDetails.getGid();
			this.tableId = tableId;
			this.gameTemplates = templateDetails;
			int maxPlayer = templateDetails.getMaxPlayer();
			this.observers = new ArrayList< PlayerInfo >();
			this.curPlayingPlayer = new ArrayList< PlayerInfo >();
			this.playingPlayerMap = new ConcurrentHashMap< Long, PlayerInfo >();
			seats = Arrays.asList( new SeatInfo[maxPlayer] );
			for( int i = 0; i < maxPlayer; i++ )
			{
				seats.set( i, new SeatInfo( i ) );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}

	protected synchronized boolean addSeatedPlayer( PlayerInfo p )
	{
		boolean succ = false;

		try
		{
			succ = observers.add( p );
			playingPlayerMap.put( p.getUserId(), p );
			System.out.println( "playingPlayerMap" + playingPlayerMap );
		}
		catch( Exception e )
		{
			e.printStackTrace();

		}
		return succ;

	}

	private synchronized boolean removedSeatedPlayer( PlayerInfo p )
	{
		boolean succ = false;
		try
		{
			succ = observers.remove( p );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return succ;
	}

	private synchronized boolean removedSeatedPlayer( Long playerId )
	{
		boolean succ = false;
		try
		{
			PlayerInfo p = playingPlayerMap.get( playerId );
			if( p == null )
			{
				return false;
			}
			succ = observers.remove( p );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return succ;
	}

	public synchronized boolean addAllSeatedToPlaying()
	{
		boolean succ = false;
		try
		{
			curPlayingPlayer.addAll( observers );
			observers.clear();

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return succ;
	}

	private synchronized boolean removedPlayingPlayer( PlayerInfo p )
	{
		boolean succ = false;
		succ = curPlayingPlayer.remove( p );
		playingPlayerMap.remove( p.getUserId() );
		return succ;
	}

	private synchronized boolean removedPlayingPlayer( Long playerId )
	{
		boolean succ = false;
		PlayerInfo p = playingPlayerMap.remove( playerId );
		if( p != null )
			succ = curPlayingPlayer.remove( p );
		return succ;
	}

	public synchronized int whatTypeOfPlayer( long playerId )
	{

		int type = 0;
		try
		{
			for( int i = 0; i < observers.size(); i++ )
			{
				PlayerInfo p = observers.get( i );
				if( p != null && p.getUserId() == playerId )
				{
					type = PlayerGlobals.OBSERVER;
					break;
				}
			}
			for( int i = 0; i < curPlayingPlayer.size(); i++ )
			{
				PlayerInfo p = curPlayingPlayer.get( i );
				if( p != null && p.getUserId() == playerId )
				{
					type = PlayerGlobals.PLAYING;
					break;
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return type;
	}

	private synchronized boolean removePlayer( PlayerInfo p )
	{
		boolean succ = false;
		try
		{

			if( observers.contains( p ) )
				succ = observers.remove( p );
			else if( curPlayingPlayer.contains( p ) )
				succ = curPlayingPlayer.remove( p );
			playingPlayerMap.remove( p.getUserId() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return succ;
	}

	private synchronized boolean removePlayer( Long playerId )
	{
		boolean succ = false;
		try
		{

			playingPlayerMap.remove( playerId );
			PlayerInfo removePlayer = null;
			for( int i = 0; i < observers.size(); i++ )
			{
				PlayerInfo p = observers.get( i );
				if( p != null && p.getUserId() == playerId )
				{
					removePlayer = p;
				}
			}
			if( removePlayer != null )
			{
				succ = observers.remove( removePlayer );
			}
			else
			{
				for( int i = 0; i < curPlayingPlayer.size(); i++ )
				{
					PlayerInfo p = curPlayingPlayer.get( i );
					if( p != null && p.getUserId() == playerId )
					{
						removePlayer = p;
					}
				}
				if( removePlayer != null )
				{
					succ = curPlayingPlayer.remove( removePlayer );
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return succ;
	}

	public int takeAnySeat( PlayerInfo p, long bal )
	{
		int seatid = -1;
		synchronized( this )
		{
			if( isPlayerAlreadyPresent( p.getUserId() ) )
			{
				SeatInfo seat = getSeat( p.getUserId() );
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
						if( seat.getState() == SeatsGlobals.EMPTY )
						{
							SeatPlayerInfo info = new SeatPlayerInfo( p.getUserId(), p.getUserName(), p.getAvatarId() );
							seat.setPlayer( info );
							seat.setState( SeatsGlobals.OCCUPIED );
							seatid = seat.getId();
							addSeatedPlayer( p );
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

	public synchronized boolean takeSeat( PlayerInfo p, long bal, int id )
	{
		boolean succ = false;
		try
		{

			SeatInfo seat = seats.get( id );
			if( seat.getState() == SeatsGlobals.EMPTY )
			{
				SeatPlayerInfo info = new SeatPlayerInfo( p.getUserId(), p.getUserName(), p.getAvatarId() );
				seat.setPlayer( info );
				seat.setState( SeatsGlobals.OCCUPIED );
				addSeatedPlayer( p );
				succ = true;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return succ;
	}

	public boolean isPlayerAlreadyPresent( long playerId )
	{

		return playingPlayerMap.containsKey( playerId );
	}

	public ArrayList< PlayerInfo > getPlayingPlayer()
	{
		return ( ArrayList< PlayerInfo > ) curPlayingPlayer;
	}

	public Integer getAvlSeat()
	{
		return( seats.size() - ( observers.size() + curPlayingPlayer.size() ) );
	}

	public SeatInfo getSeat( long playerId )
	{
		Iterator< SeatInfo > seatIterator = seats.iterator();
		SeatInfo seat = null;
		try
		{

			while( seatIterator.hasNext() )
			{
				SeatInfo s = seatIterator.next();
				if( s.getPlayer() != null && playerId == s.getPlayer().getUserId() )
				{
					seat = s;
					break;
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return seat;
	}

	public synchronized boolean removePlayerSeat( long playerId )
	{
		boolean succ = false;
		try
		{

			SeatInfo s = getSeat( playerId );
			if( s != null )
			{
				s.removePlayer();
			}
			succ = removePlayer( playerId );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return succ;
	}

	public int getSeatedPlayerSize()
	{
		return observers.size();
	}

	public int getPlayingPlayerSize()
	{
		return curPlayingPlayer.size();
	}

	public abstract boolean startGame();

	public abstract boolean endGame();

	public abstract void setLastGame( boolean isLastGame );

	public abstract boolean joinPlayer( PlayerInfo info, PlayerSession playerSession );

	public abstract boolean userLeft( long playerId, int playerRequestedType );

	public long getTableId()
	{
		return tableId;
	}

	public List< SeatInfo > getSeats()
	{
		return seats;
	}

	public ArrayList< Long > getAllplayer()
	{

		Set< Long > st = playingPlayerMap.keySet();
		return new ArrayList< Long >( st );
	}

	public int getStatus()
	{
		return status;
	}

	public GameTemplates getGameTemplates()
	{
		return gameTemplates;
	}
	
	

}
