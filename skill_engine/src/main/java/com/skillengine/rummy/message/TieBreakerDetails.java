package com.skillengine.rummy.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
@Getter
public class TieBreakerDetails extends Message
{
	private List< Long > eliminatedPlayers;
	private long time;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param eliminatedPlayers
	 * @param time
	 */
	public TieBreakerDetails( long tableId, List< Long > eliminatedPlayers, long time )
	{
		super( 1, MessageConstants.TIE_BREAKER, tableId );
		this.eliminatedPlayers = eliminatedPlayers;
		this.time = time;
	}

}
