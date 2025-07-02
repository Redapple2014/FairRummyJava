package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
@Getter
public class DealScoreCardDetails extends Message
{
	private long playerId;
	private String playerName;
	private int totalPoints;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param playerName
	 * @param totalPoints
	 */
	public DealScoreCardDetails( long tableId, long playerId, String playerName, int totalPoints )
	{
		super( 1, MessageConstants.DEAL_SCORE_CARD_DETAILS, tableId );
		this.playerId = playerId;
		this.playerName = playerName;
		this.totalPoints = totalPoints;
	}

}
