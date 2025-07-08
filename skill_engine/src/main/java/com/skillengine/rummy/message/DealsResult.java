package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties( ignoreUnknown = true )
public class DealsResult extends Message
{
	private long winnerId;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param winnerId
	 */
	public DealsResult( long tableId, long winnerId )
	{
		super( 1, MessageConstants.DEAL_RESULT, tableId );
		this.winnerId = winnerId;
	}

}
