package com.skillengine.rummy.message;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FMGResponse extends Message
{

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public FMGResponse( long tableId )
	{
		super( 1, MessageConstants.FMG_RESPONSE, tableId );
	}

}
