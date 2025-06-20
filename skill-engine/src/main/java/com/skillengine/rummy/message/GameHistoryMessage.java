package com.skillengine.rummy.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Game history message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class GameHistoryMessage extends Message {

    private final long userId;

    /**
     * Constructor
     *
     * @param userId User id
     */
    public GameHistoryMessage(long userId) {
        super(1, MessageConstants.GAME_HISTORY, 0L);

        this.userId = userId;
    }
}