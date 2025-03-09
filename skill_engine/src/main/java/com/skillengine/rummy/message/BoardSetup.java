package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.BOARD_SETUP;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@JsonIgnoreProperties( ignoreUnknown = true )
@NoArgsConstructor
public class BoardSetup extends Message
{
	public BoardSetup( int serviceType, String msgType, long tableId )
	{
		super( serviceType, BOARD_SETUP, tableId );
	}

}
