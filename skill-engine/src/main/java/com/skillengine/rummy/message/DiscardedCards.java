package com.skillengine.rummy.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skillengine.rummy.cards.CardId;

import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties( ignoreUnknown = true )
@NoArgsConstructor
@Getter
public class DiscardedCards extends Message
{
	private List< String > discardedCardId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param discardedCardId
	 */
	public DiscardedCards( long tableId, List< String > discardedCardId )
	{
		super( 1, MessageConstants.DISCARDED_CARDS, tableId );
		this.discardedCardId = discardedCardId;
	}

}
