package com.skillengine.rummy.message;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DealScoreCard extends Message
{
	private List< DealScoreCardDetails > dealScoreDetails;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param dealScoreDetails
	 */
	public DealScoreCard( long tableId, List< DealScoreCardDetails > dealScoreDetails )
	{
		super( 1, MessageConstants.DEAL_SCORE_CARD_DETAILS_LIST, tableId );
		this.dealScoreDetails = dealScoreDetails;
	}

}
