package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
	public FinishPlayerTimeOut( int serviceType, String msgType, long tableId, long playerId )
	{
		super( 1, MessageConstants.FINISH_PLAYER_TIMEOUT, tableId );
		this.playerId = playerId;
	}

}
