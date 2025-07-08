package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
@Getter
@ToString
public class DealScoreCardDetails extends Message
{
	private long playerId;
	private String playerName;
	private int totalPoints;
	private boolean isWinner;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param playerName
	 * @param totalPoints
	 */
	public DealScoreCardDetails( long tableId, long playerId, String playerName, int totalPoints, boolean isWinner )
	{
		super( 1, MessageConstants.DEAL_SCORE_CARD_DETAILS, tableId );
		this.playerId = playerId;
		this.playerName = playerName;
		this.totalPoints = totalPoints;
		this.isWinner = isWinner;
	}

}
