package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PickCloseDeckOutBound extends Message
{
	private long gPlayerId;
	private String pickedCard;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param gPlayerId
	 * @param pickedCard
	 */
	public PickCloseDeckOutBound( long tableId, long gPlayerId, String pickedCard )
	{
		super( 1, MessageConstants.PICK_CLOSED_DECK_OUTBOUND, tableId );
		this.gPlayerId = gPlayerId;
		this.pickedCard = pickedCard;
	}

}
