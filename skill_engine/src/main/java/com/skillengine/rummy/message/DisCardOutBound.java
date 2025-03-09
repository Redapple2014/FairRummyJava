package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DisCardOutBound extends Message
{
	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param discardCardId
	 */
	public DisCardOutBound( long tableId, long playerId, String discardCardId )
	{
		super( 1, MessageConstants.DISCARD_OUTBOUND, tableId );
		this.playerId = playerId;
		this.discardCardId = discardCardId;
	}

	private long playerId;
	private String discardCardId;

}
