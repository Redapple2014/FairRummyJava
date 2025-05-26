package com.connection.client.msg;

import com.connection.msg.MessageConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PingRequest extends Message
{
	private long curTime;

	/**
	 * @param msgType
	 * @param userId
	 * @param curTime
	 */
	public PingRequest( String msgType, long userId, long curTime )
	{
		super( MessageConstants.PING_REQUEST, userId );
		this.curTime = curTime;
	}

}
