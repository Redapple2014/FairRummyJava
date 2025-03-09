package com.skillengine.rummy.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.message.parsers.Jackson;
import com.skillengine.service.message.ServiceMessage;
import com.skillengine.sessions.PlayerSession;

public class MessageDispatcher
{
	private Jackson parse;
	private final String QUEUE_NAME = "cs";

	public MessageDispatcher( Jackson jacksonParsers )
	{
		this.parse = jacksonParsers;
	}

	private Map< Long, PlayerSession > playerSessionMap = new ConcurrentHashMap<>();

	private Map< Long, Long > blockedUsers = new ConcurrentHashMap<>();

	public void addSession( PlayerSession details )
	{
		playerSessionMap.put( details.getUserID(), details );
	}

	public void sendMessage( List< Long > players, List< Message > msg )
	{
		for( Long playerId : players )
		{
			if( !isBlocked( playerId ) )
			{
				PlayerSession playerSession = playerSessionMap.get( playerId );
				int serviceId = playerSession.getServiceId();
				ServiceMessage message = new ServiceMessage( parse.writeValueAsString( msg ), parse.writeValueAsString( playerSession ), -1l );
				String finalMsg = parse.writeValueAsString( message );
				SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
			}
		}
	}

	public void sendMessage( long playerId, List< Message > msg )
	{
		if( !isBlocked( playerId ) )
		{
			PlayerSession playerSession = playerSessionMap.get( playerId );
			int serviceId = playerSession.getServiceId();
			ServiceMessage message = new ServiceMessage( parse.writeValueAsString( msg ), parse.writeValueAsString( playerSession ), -1l );
			String finalMsg = parse.writeValueAsString( message );
			SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
		}
	}

	public void sendMessage( long playerId, Message msg )
	{
		List< Message > message = new ArrayList<>();
		message.add( msg );
		if( !isBlocked( playerId ) )
		{
			System.out.println( "sendMessage playerId " + playerId );
			PlayerSession playerSession = playerSessionMap.get( playerId );
			int serviceId = playerSession.getServiceId();
			ServiceMessage srvMsg = new ServiceMessage( parse.writeValueAsString( message ), parse.writeValueAsString( playerSession ), -1l );
			String finalMsg = parse.writeValueAsString( srvMsg );
			SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
		}
	}

	public void sendMessage( List< Long > playerId, Message msg )
	{
		List< Message > message = new ArrayList<>();
		message.add( msg );
		for( long uId : playerId )
		{
			if( !isBlocked( uId ) )
			{
				PlayerSession playerSession = playerSessionMap.get( uId );
				int serviceId = playerSession.getServiceId();
				ServiceMessage srvMsg = new ServiceMessage( parse.writeValueAsString( message ), parse.writeValueAsString( playerSession ), -1l );
				String finalMsg = parse.writeValueAsString( srvMsg );
				SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
			}
		}
	}

	public PlayerSession removeSession( long playerId )
	{
		return playerSessionMap.remove( playerId );
	}

	public void sendMessage( PlayerSession playerSession, Message message )
	{
		List< Message > msg = new ArrayList<>();
		msg.add( message );
		int serviceId = playerSession.getServiceId();
		ServiceMessage srvMsg = new ServiceMessage( parse.writeValueAsString( msg ), parse.writeValueAsString( playerSession ), -1l );
		String finalMsg = parse.writeValueAsString( srvMsg );
		SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
	}

	private boolean isBlocked( long playerId )
	{
		return blockedUsers.get( playerId ) != null ? true : false;
	}

	public void addBlockMsg( long playerId )
	{
		blockedUsers.put( playerId, playerId );

	}

	public void sendMessageWithOutBlockCheck( long playerId, List< Message > message )
	{
		PlayerSession playerSession = playerSessionMap.get( playerId );
		if( playerSession == null )
		{
			return;
		}
		System.out.println( "sendMessageWithOutBlockCheck" + message );
		int serviceId = playerSession.getServiceId();
		ServiceMessage srvMsg = new ServiceMessage( parse.writeValueAsString( message ), parse.writeValueAsString( playerSession ), -1l );
		String finalMsg = parse.writeValueAsString( srvMsg );
		SkillEngineImpl.getInstance().getFrameworkImpl().publishToQueue( QUEUE_NAME, finalMsg );
	}

	public void removeBlock( long playerId )
	{
		blockedUsers.remove( playerId );
	}
}
