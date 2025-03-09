package com.connection.message.queue;

import com.connection.main.ConnectionServiceImpl;
import com.connection.services.PlayerSession;
import com.connection.services.ServiceMessage;

public class CSMessageHandler implements MessageHandler
{

	@Override
	public void handleMessage( String message )
	{
		ServiceMessage serviceMessage = ConnectionServiceImpl.getInstance().getJacksonObjectWrapper().readValue( message, ServiceMessage.class );
		PlayerSession playerSession = ConnectionServiceImpl.getInstance().getJacksonObjectWrapper().readValue( serviceMessage.getPlayerSession(), PlayerSession.class );
		ConnectionServiceImpl.getInstance().getMessageDispatcher().sendMessage( playerSession, serviceMessage.getGamePayload() );

	}

}
