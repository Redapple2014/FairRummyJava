package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Declare extends Message
{
	private List< List< String > > cardsGrouping;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param cardsGrouping
	 */
	public Declare( long tableId, List< List< String > > cardsGrouping )
	{
		super( 1, MessageConstants.USER_DECLARE, tableId );
		this.cardsGrouping = cardsGrouping;
	}

}
