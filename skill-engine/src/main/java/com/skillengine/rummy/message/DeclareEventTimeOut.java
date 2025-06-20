package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeclareEventTimeOut extends Message
{
	private long timeLeft;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public DeclareEventTimeOut( long tableId, long timeLeft )
	{
		super( 1, MessageConstants.DECLARE_TIME_OUT, tableId );
		this.timeLeft = timeLeft;
	}

}
