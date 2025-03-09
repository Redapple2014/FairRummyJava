package com.connection.client.msg.handler;

import com.connection.client.msg.HandShakeRequest;
import com.connection.client.msg.HandShakeResponse;
import com.connection.jackson.JacksonObjectWrapper;
import com.connection.main.ConnectionServiceImpl;
import com.connection.msg.handler.MessageHandler;
import com.connection.services.PlayerSession;

public class HandShakeHandler implements MessageHandler< HandShakeRequest >
{

	private JacksonObjectWrapper jacksonObjectWrapper;

	public HandShakeHandler( JacksonObjectWrapper jacksonObjectWrapper )
	{
		this.jacksonObjectWrapper = jacksonObjectWrapper;
	}

	@Override
	public void handleMessage( PlayerSession session, HandShakeRequest message )
	{
		HandShakeResponse handShakeResponse = new HandShakeResponse( session.getUserID(), "Sending the userId " + message.getUserId() );
		ConnectionServiceImpl.getInstance().getMessageDispatcher().sendMessage( session, jacksonObjectWrapper.writeValueAsString( handShakeResponse ) );
	}

}
