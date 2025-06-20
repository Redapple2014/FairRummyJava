package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.Message;
import com.skillengine.sessions.PlayerSession;

public interface MessageHandler< T extends Message >
{
	public void handleMessage( PlayerSession session, T message, long tableId );
}
