package com.connection.netty.channels;

import io.netty.channel.Channel;

public class ChannelInfo
{
	private long channelID;
	private Channel channel;
	private String cookieId;
	private int serviceId;

	public ChannelInfo( long channelID, Channel channel, int serviceId )
	{
		this.channelID = channelID;
		this.channel = channel;
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceId
	 */
	public int getServiceId()
	{
		return serviceId;
	}

	/**
	 * @param serviceId
	 *                the serviceId to set
	 */
	public void setServiceId( int serviceId )
	{
		this.serviceId = serviceId;
	}

	/**
	 * @return the channelID
	 */
	public long getChannelID()
	{
		return channelID;
	}

	/**
	 * @param channelID
	 *                the channelID to set
	 */
	public void setChannelID( long channelID )
	{
		this.channelID = channelID;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/**
	 * @param channel
	 *                the channel to set
	 */
	public void setChannel( Channel channel )
	{
		this.channel = channel;
	}

	/**
	 * @return the cookieId
	 */
	public String getCookieId()
	{
		return cookieId;
	}

	/**
	 * @param cookieId
	 *                the cookieId to set
	 */
	public void setCookieId( String cookieId )
	{
		this.cookieId = cookieId;
	}

}
