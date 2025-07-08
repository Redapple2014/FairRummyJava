package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class DealScoreRequest extends Message
{
	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public DealScoreRequest( long tableId )
	{
		super( 1, MessageConstants.DEAL_SCORE_CARD_REQ, tableId );
	}

}
