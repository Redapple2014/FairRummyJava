package org.fcesur.cs.handler;

import org.fcesur.cs.message.Message;
import org.fcesur.cs.services.PlayerSession;


public interface MessageHandler<T extends Message> {
    public void handleMessage(PlayerSession session, T message);
}
