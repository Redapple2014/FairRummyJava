package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DropResponse extends Message {
    private long playingPlayerId;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param playingPlayerId
     */
    public DropResponse(long tableId, long playingPlayerId) {
        super(1, MessageConstants.DROP_RESPONSE, tableId);
        this.playingPlayerId = playingPlayerId;
    }

}
