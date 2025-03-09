package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinishInited extends Message
{
	private long playerId;
	private String discardId;
	private long timeLeft;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 */
	public FinishInited( long tableId, long playerId, String discardId, long timeLeft )
	{
		super( 1, MessageConstants.FINISH_INIT, tableId );
		this.playerId = playerId;
		this.discardId = discardId;
		this.timeLeft = timeLeft;
	}

}
