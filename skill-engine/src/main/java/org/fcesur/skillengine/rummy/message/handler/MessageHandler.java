package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.rummy.message.Message;
import org.fcesur.skillengine.sessions.PlayerSession;

public interface MessageHandler<T extends Message> {
    public void handleMessage(PlayerSession session, T message, long tableId);
}
