package org.fcesur.skillengine.rummy.message.handler;

import org.fcesur.skillengine.SkillEngine;
import org.fcesur.skillengine.repository.GameRepository;
import org.fcesur.skillengine.rummy.message.GameHistoryRequest;
import org.fcesur.skillengine.rummy.message.GameHistoryResponse;
import org.fcesur.skillengine.rummy.message.ScoreUpdate2;
import org.fcesur.skillengine.sessions.PlayerSession;
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
        List<ScoreUpdate2> scoreUpdates =
              GameRepository.getInstance().history(message.getUserId(), message.getLimit());

        // build response
        GameHistoryResponse response = new GameHistoryResponse(message.getTableId(), scoreUpdates);

        // send response
        SkillEngine.getInstance().getDispatcher().sendMessage(session, response);
    }
}
