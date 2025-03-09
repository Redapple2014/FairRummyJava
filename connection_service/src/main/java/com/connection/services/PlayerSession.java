package com.connection.services;

import java.io.Serializable;

public class PlayerSession implements Serializable
{

	private static final long serialVersionUID = -2382616042872210756L;
	private long channelID;
	private long userID;
	private int serviceId;

	/**
	 * @return the serviceId
	 */
	public int getServiceId()
	{
		return serviceId;
	}

	public PlayerSession()
	{
	}

	public PlayerSession( long channelID, long userId, int serviceId )
	{
		this.channelID = channelID;
		this.userID = userId;
		this.serviceId = serviceId;
	}

	public long getUserID()
	{
		return userID;
	}

	public long getChannelID()
	{
		return channelID;
	}

	@Override
	public String toString()
	{
		return "PlayerSession [channelID=" + channelID + ", userID=" + userID + "]";
	}

}
