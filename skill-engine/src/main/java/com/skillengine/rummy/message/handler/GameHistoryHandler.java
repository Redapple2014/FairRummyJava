package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngine;
import com.skillengine.repository.GameRepository;
import com.skillengine.rummy.message.GameHistoryRequest;
import com.skillengine.rummy.message.GameHistoryResponse;
import com.skillengine.rummy.message.ScoreUpdate;
import com.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.util.List;

@Slf4j
public final class GameHistoryHandler implements MessageHandler<GameHistoryRequest> {

    @Override
    public void handleMessage(@NonNull PlayerSession session, @NonNull GameHistoryRequest message, long tableId) {

        // log
        log.info("Message Received: {}", message);

        // get scores from database
        List<ScoreUpdate> scoreUpdates =
              GameRepository.getInstance().history(message.getUserId(), message.getLimit());

        // build response
        GameHistoryResponse response = new GameHistoryResponse(scoreUpdates);

        // send response
        SkillEngine.getInstance().getDispatcher().sendMessage(session, response);
    }
}
