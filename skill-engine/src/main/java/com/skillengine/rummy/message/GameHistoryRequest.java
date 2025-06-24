package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Game history message
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class GameHistoryRequest extends Message {

    @JsonProperty("user_id")
    private final long userId;

    @JsonProperty("limit")
    private final int limit;

    public GameHistoryRequest() {
        super(1, MessageConstants.GAME_HISTORY, 0L);

        this.userId = 0;
        this.limit = 5;
    }

    /**
     * Constructor
     *
     * @param userId User id
     * @param limit  Limit
     */
    public GameHistoryRequest(long userId, int limit) {
        super(1, MessageConstants.GAME_HISTORY, 0L);

        this.userId = userId;
        this.limit = limit;
    }
}