package com.connection.client.msg;

import com.connection.msg.MessageConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PingResponse extends Message
{
	private long curTime;

	/**
	 * @param msgType
	 * @param userId
	 * @param curTime
	 */
	public PingResponse( long userId, long curTime )
	{
		super( MessageConstants.PING_REPONSE, userId );
		this.curTime = curTime;
	}

}
