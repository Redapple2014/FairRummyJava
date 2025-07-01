package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameSetup extends Message
{
	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public GameSetup( long tableId )
	{
		super( 1, MessageConstants.GAME_SETUP, tableId );
	}

	private List< List< String > > handCard;
	private long dealerId;
	private String jokerCard;
	private List< String > openDeck;
	private float closedDeckSize;
	private long gameId;
	private int currentDealNo;
}
