package com.fcesur.cs.handler;

import com.fcesur.cs.message.Message;
import com.fcesur.cs.services.PlayerSession;


public interface MessageHandler<T extends Message> {
    public void handleMessage(PlayerSession session, T message);
}
