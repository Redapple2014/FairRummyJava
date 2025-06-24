package com.skillengine.repository;

import com.skillengine.rummy.message.ScoreUpdate;
import com.skillengine.rummy.message.UserScore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class GameRepositoryTest {

    @Test
    void save() {

        GameRepository gameRepository = GameRepository.getInstance();

        ScoreUpdate scoreUpdate = new ScoreUpdate(1L, "1", List.of(
              UserScore.builder().playingPlayerId(1L).score(1).status(1).txnAmt(1).cardIds(List.of(List.of("1", "2"))).build(),
              UserScore.builder().playingPlayerId(2L).score(1).status(1).txnAmt(1).cardIds(List.of(List.of("1", "2"))).build()
        ));

        gameRepository.save(scoreUpdate);
    }

    @Test
    void history() {

        GameRepository gameRepository = GameRepository.getInstance();

        var history = gameRepository.history(1L, 10);

        log.info("History: {}", history);
    }
}