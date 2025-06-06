package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@JsonIgnoreProperties( ignoreUnknown = true )
@NoArgsConstructor
public class BoardStatus extends Message
{
	private int status;
	private int timeDiff;

	public BoardStatus( long tableId, int status, int timediff )
	{
		super( 1, "boardstatus", tableId );
		this.status = status;
		this.timeDiff = timediff;
	}

	public int getStatus()
	{
		return status;
	}

	public int getTimeDiff()
	{
		return timeDiff;
	}

}
