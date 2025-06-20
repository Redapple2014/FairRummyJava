package com.skillengine.rummy.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties( ignoreUnknown = true )
public class SetHandCards extends Message
{
	private List< List< String > > cards;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param groupedHandCards
	 */
	public SetHandCards( long tableId, List< List< String > > groupedHandCards )
	{
		super( 1, MessageConstants.SET_HAND_CARDS, tableId );
		this.cards = groupedHandCards;
	}

}
