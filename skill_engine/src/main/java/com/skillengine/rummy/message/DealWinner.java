package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DealWinner extends Message
{
	private long winningPlayerId;
	private double winningAmount;
	private boolean winningHappened;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param winningAmount
	 */
	public DealWinner( long tableId, long playerId, double winningAmount, boolean winningHappened )
	{
		super( 1, MessageConstants.DEAL_WINNER, tableId );
		this.winningPlayerId = playerId;
		this.winningAmount = winningAmount;
		this.winningHappened = winningHappened;
	}

}
