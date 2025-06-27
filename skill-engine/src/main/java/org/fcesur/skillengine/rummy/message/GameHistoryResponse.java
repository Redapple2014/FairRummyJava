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

    @JsonProperty("variant")
    private final int variant;

    @JsonProperty("score_history")
    private final List<ScoreUpdate2> scoreHistory;

    /**
     * Constructor
     */
    public GameHistoryResponse(long tableId, int variant, List<ScoreUpdate2> scoreHistory) {
        super(1, MessageConstants.GAME_HISTORY_RESPONSE, tableId);

        this.variant = variant;
        this.scoreHistory = scoreHistory;
    }
}