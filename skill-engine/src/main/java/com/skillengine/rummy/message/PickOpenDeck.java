package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PickOpenDeck extends Message
{
	private List< List< String > > groupCards;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param groupCards
	 */
	public PickOpenDeck( long tableId, List< List< String > > groupCards )
	{
		super( 1, MessageConstants.PICK_OPEN_DECK, tableId );
		this.groupCards = groupCards;
	}

}
