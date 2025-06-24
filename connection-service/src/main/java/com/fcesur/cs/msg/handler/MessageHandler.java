package com.fcesur.cs.msg.handler;

import com.fcesur.cs.client.msg.Message;
import com.fcesur.cs.services.PlayerSession;


public interface MessageHandler<T extends Message> {
    public void handleMessage(PlayerSession session, T message);
}
