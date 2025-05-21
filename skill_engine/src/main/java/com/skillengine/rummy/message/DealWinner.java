package com.skillengine.rummy.message;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DealWinner extends Message
{
	private long winningPlayerId;
	private double winningAmount;
	private boolean winningHappened;
	private List< List< String > > grpCardIds;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerId
	 * @param winningAmount
	 */
	public DealWinner( long tableId, long playerId, double winningAmount, boolean winningHappened, List< List< String > > grpCards )
	{
		super( 1, MessageConstants.DEAL_WINNER, tableId );
		this.winningPlayerId = playerId;
		this.winningAmount = winningAmount;
		this.winningHappened = winningHappened;
		this.grpCardIds = grpCards;
	}

}
