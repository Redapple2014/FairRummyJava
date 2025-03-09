package com.connection.client.msg;

import com.connection.msg.MessageConstants;

import lombok.Data;

@Data
public class HandShakeResponse extends Message
{

	private String dummyText;

	public HandShakeResponse( long userId, String dummyText )
	{
		super( MessageConstants.HAND_SHAKE_RESPONSE, userId );
		this.dummyText = dummyText;
	}

}
