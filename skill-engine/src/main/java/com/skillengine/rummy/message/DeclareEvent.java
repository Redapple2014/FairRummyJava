package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeclareEvent extends Message
{
	private int timeLeft;

	public DeclareEvent( long tableId, int timeLeft )
	{
		super( 1, MessageConstants.DECLARE_SERVER, tableId );
		this.timeLeft = timeLeft;
	}

}
