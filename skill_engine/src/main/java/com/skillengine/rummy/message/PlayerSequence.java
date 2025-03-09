package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerSequence extends Message
{
	private List< Long > playerIdList;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param playerIdList
	 */
	public PlayerSequence( long tableId, List< Long > playerIdList )
	{
		super( 1, MessageConstants.PLAYER_SEQ, tableId );
		this.playerIdList = playerIdList;
	}

}
