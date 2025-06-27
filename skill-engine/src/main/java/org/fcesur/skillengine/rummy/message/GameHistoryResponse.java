package org.fcesur.skillengine.rummy.message;

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

    @JsonProperty("score_history")
    private final List<ScoreUpdate2> scores;

    /**
     * Constructor
     */
    public GameHistoryResponse(long tableId, List<ScoreUpdate2> scores) {
        super(1, MessageConstants.GAME_HISTORY_RESPONSE, tableId);

        this.scores = scores;
    }
}