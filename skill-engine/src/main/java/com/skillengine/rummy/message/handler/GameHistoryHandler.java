package com.skillengine.rummy.message.handler;

import com.skillengine.rummy.message.GameHistoryMessage;
import com.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

@Slf4j
public final class GameHistoryHandler implements MessageHandler<GameHistoryMessage> {

    @Override
    public void handleMessage(@NonNull PlayerSession session, @NonNull GameHistoryMessage message, long tableId) {
        log.info("Game History Message: {}", message);
    }
}
