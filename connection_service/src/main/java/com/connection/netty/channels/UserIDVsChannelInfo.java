package com.connection.netty.channels;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.connection.services.PlayerSession;

public class UserIDVsChannelInfo
{
	private static final Map< Long, PlayerSession > userVsChannelInfo = new ConcurrentHashMap<>();

	public static PlayerSession put( long userId, long channelId, int serviceId )
	{
		PlayerSession playerSession = new PlayerSession( channelId, userId, serviceId );
		userVsChannelInfo.put( userId, playerSession );
		return playerSession;
	}

	public static PlayerSession getPlayerSession( long userId )
	{
		return userVsChannelInfo.get( userId );
	}

	public static PlayerSession removePlayerSession( long userId )
	{
		return userVsChannelInfo.remove( userId );
	}
}
