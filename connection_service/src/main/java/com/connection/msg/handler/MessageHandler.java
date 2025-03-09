package com.connection.msg.handler;

import com.connection.client.msg.Message;
import com.connection.services.PlayerSession;


public interface MessageHandler< T extends Message >
{
	public void handleMessage( PlayerSession session, T message );
}
