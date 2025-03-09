
package com.connection.msg;

import static com.connection.services.ServiceTypes.CONNECTION_SERVICE;
import static com.connection.services.ServiceTypes.GAME_SERVICE;

import com.connection.client.msg.HandShakeRequest;
import com.connection.client.msg.handler.HandShakeHandler;
import com.connection.jackson.JacksonObjectWrapper;
import com.connection.main.ConnectionServiceImpl;
import com.connection.services.PlayerSession;
import com.connection.services.ServiceMessage;

public class MessageDigester
{
	private JacksonObjectWrapper jacksonObjectWrapper;

	public MessageDigester( JacksonObjectWrapper jacksonObjectWrapper )
	{
		super();
		this.jacksonObjectWrapper = jacksonObjectWrapper;
	}

	public void messageProcessor( PlayerSession playerSession, Frames frames, MessageParser messageParser )
	{
		switch( frames.getServiceType() )
		{
		case CONNECTION_SERVICE:
			processCSMessage( playerSession, frames, messageParser );
			break;
		case GAME_SERVICE:
			procesGEMessage( playerSession, messageParser, frames );
			break;
		}
	}

	private void processCSMessage( PlayerSession playerSession, Frames frames, MessageParser messageParser )
	{
		try
		{
			String msgType = jacksonObjectWrapper.getMsgType( messageParser.getSrvMsg() );
			switch( msgType )
			{
			case MessageConstants.HAND_SHAKE:
				HandShakeHandler handler = new HandShakeHandler( jacksonObjectWrapper );
				handler.handleMessage( playerSession, jacksonObjectWrapper.readValue( messageParser.getSrvMsg(), HandShakeRequest.class ) );
				break;
			}
		}
		catch( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void procesGEMessage( PlayerSession playerSession, MessageParser messageParser, Frames frames )
	{
		try
		{
			System.out.println( "GE Message" + messageParser );
			String userSession = jacksonObjectWrapper.writeValueAsString( playerSession );
			ServiceMessage message = new ServiceMessage( messageParser.getSrvMsg(), userSession, frames.getReceiverId() );
			ConnectionServiceImpl.getInstance().getFrameworkImpl().publishToQueue( "ge", jacksonObjectWrapper.writeValueAsString( message ) );

		}
		catch( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
