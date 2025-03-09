package com.skillengine.rummy.message;

public class Drop extends Message
{

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public Drop( long tableId )
	{
		super( 1, MessageConstants.DROP, tableId );
	}

}
