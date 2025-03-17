package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class HandDiscardInfo extends Message
{

	private long playingPlayerId;
	private String discardCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playingPlayerId
	 * @param discardCardId
	 */
	public HandDiscardInfo( long tableId, long playingPlayerId, String discardCardId )
	{
		super( 1, MessageConstants.PLAYER_TIME_OUT_DISCARD, tableId );
		this.playingPlayerId = playingPlayerId;
		this.discardCardId = discardCardId;
	}

}
