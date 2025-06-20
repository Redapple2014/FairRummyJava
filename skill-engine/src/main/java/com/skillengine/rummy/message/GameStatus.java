package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.GAME_STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatus extends Message
{
	private int status;
	private int timeLeft;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param status
	 * @param timeLeft
	 */
	public GameStatus( long tableId, int status, int timeLeft )
	{
		super( 1, GAME_STATUS, tableId );
		this.status = status;
		this.timeLeft = timeLeft;
	}

}
