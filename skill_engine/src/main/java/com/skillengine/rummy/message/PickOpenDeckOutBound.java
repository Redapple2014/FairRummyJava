package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PickOpenDeckOutBound extends Message
{
	private String pickedCard;
	private boolean isJokerCard;
	private long playingPlayerId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param pickedCard
	 * @param isJokerCard
	 */
	public PickOpenDeckOutBound( long tableId, String pickedCard, boolean isJokerCard, long playingPlayerId )
	{
		super( 1, MessageConstants.PICK_OPEN_DECK_OUTBOUND, tableId );
		this.pickedCard = pickedCard;
		this.isJokerCard = isJokerCard;
		this.playingPlayerId = playingPlayerId;
	}

}
