package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinishPlayerTimeOut extends Message
{
	private long playerId;
	private String finishCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 */
	public FinishPlayerTimeOut( long tableId, long playerId, String finishCardId )
	{
		super( 1, MessageConstants.FINISH_PLAYER_TIMEOUT, tableId );
		this.playerId = playerId;
		this.finishCardId = finishCardId;
	}

}
