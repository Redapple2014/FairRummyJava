package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Game history message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class GameHistoryResponse extends Message {

    @JsonProperty("scores")
    private final List<ScoreUpdate> scoreUpdates;

    /**
     * Constructor
     */
    public GameHistoryResponse(List<ScoreUpdate> scoreUpdates) {
        super(1, MessageConstants.GAME_HISTORY_RESPONSE, 0L);

        this.scoreUpdates = scoreUpdates;
    }
}