package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Game history message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class GameHistoryMessage extends Message {

    @JsonProperty("user_id")
    private final long userId;

    @JsonProperty("limit")
    private final int limit;

    /**
     * Constructor
     *
     * @param userId User id
     * @param limit  Limit
     */
    public GameHistoryMessage(long userId, int limit) {
        super(1, MessageConstants.GAME_HISTORY, 0L);

        this.userId = userId;
        this.limit = limit;
    }
}