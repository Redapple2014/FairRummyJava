package com.skillengine.message.queue;

import com.skillengine.main.SkillEngineImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        log.info("Received Message {}", message);
        SkillEngineImpl.getInstance().getHandler().parser(message);
    }

}
