package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Discard extends Message
{
	private long playerId;
	private String discardCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param discardCardId
	 */
	public Discard( long tableId, long playerId, String discardCardId )
	{
		super( 1, MessageConstants.DISCARD, tableId );
		this.playerId = playerId;
		this.discardCardId = discardCardId;
	}

}
