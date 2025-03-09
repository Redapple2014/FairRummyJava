package com.skillengine.rummy.message;

import lombok.Getter;

@Getter
public class PlayerTurnIntimation extends Message
{
	private long playingPlayerId;
	private long turnTimer;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playingPlayerId
	 * @param turnTimer
	 */
	public PlayerTurnIntimation( long tableId, long playingPlayerId, long turnTimer )
	{
		super( 1, MessageConstants.PLAYER_TURN, tableId );
		this.playingPlayerId = playingPlayerId;
		this.turnTimer = turnTimer;
	}

}
