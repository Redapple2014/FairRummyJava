package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerTurnTimeOut extends Message
{
	private long playingPlayerId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playingPlayerId
	 */
	public PlayerTurnTimeOut( long tableId, long playingPlayerId )
	{
		super( 1, MessageConstants.PLAYER_TURN_OUT, tableId );
		this.playingPlayerId = playingPlayerId;
	}

}
