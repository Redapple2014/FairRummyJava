package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class LeaveBoard extends Message
{

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 */
	public LeaveBoard( long tableId )
	{
		super( 1, MessageConstants.LEAVE_BOARD, tableId );
	}

}
