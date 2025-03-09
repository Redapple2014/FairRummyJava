package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Finish extends Message
{
	private List< List< String > > groupCards;
	private String discardCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param groupCards
	 * @param discardCardId
	 */
	public Finish( long tableId, List< List< String > > groupCards, String discardCardId )
	{
		super( 1, MessageConstants.FINISH, tableId );
		this.groupCards = groupCards;
		this.discardCardId = discardCardId;
	}

}
