package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TableReconReq extends Message
{
	private long userId;

	public TableReconReq( long tableId, long playerId )
	{
		super( 1, MessageConstants.TABLE_RECON_REQ, tableId );
		userId = playerId;
	}

}
