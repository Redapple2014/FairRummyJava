package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class DiscardCardReq extends Message
{
	public DiscardCardReq( long tableId )
	{
		super( 1, MessageConstants.REQUEST_DISCARD_CARDS, tableId );
	}

}
