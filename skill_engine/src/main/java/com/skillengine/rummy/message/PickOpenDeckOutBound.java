package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PickOpenDeckOutBound extends Message
{
	private String pickedCard;
	private boolean isJokerCard;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param pickedCard
	 * @param isJokerCard
	 */
	public PickOpenDeckOutBound( long tableId, String pickedCard, boolean isJokerCard )
	{
		super( 1, MessageConstants.PICK_OPEN_DECK_OUTBOUND, tableId );
		this.pickedCard = pickedCard;
		this.isJokerCard = isJokerCard;
	}

}
