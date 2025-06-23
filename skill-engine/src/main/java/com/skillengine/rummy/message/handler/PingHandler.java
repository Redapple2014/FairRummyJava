package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.PingMessage;
import com.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

@Slf4j
public final class PingHandler implements MessageHandler<PingMessage> {

    @Override
    public void handleMessage(@NonNull PlayerSession session, @NonNull PingMessage message, long tableId) {
        log.info("Ping Message Received");
    }
}
