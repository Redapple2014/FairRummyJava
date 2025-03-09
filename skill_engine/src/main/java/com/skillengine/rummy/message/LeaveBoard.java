package com.skillengine.rummy.message;

public class LeaveBoard extends Message
{

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public LeaveBoard(  long tableId )
	{
		super( 1,MessageConstants.BOARD_INFO, tableId );
	}

}
