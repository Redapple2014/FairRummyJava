package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrongFinishInfo extends Message
{
	private long playingPlayerId;
	private String finishCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playingPlayerId
	 */
	public WrongFinishInfo( long tableId, long playingPlayerId, String finishCardId )
	{
		super( 1, MessageConstants.WRONG_FINISH, tableId );
		this.playingPlayerId = playingPlayerId;
		this.finishCardId = finishCardId;
	}

}
