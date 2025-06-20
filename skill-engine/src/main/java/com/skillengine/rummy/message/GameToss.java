package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameToss extends Message {

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param gPlayerID
     * @param cardId
     */
    public GameToss(String msgType, long tableId, long gPlayerID, String cardId) {
        super(1, MessageConstants.GAME_TOSS, tableId);
        this.gamePlayerID = gPlayerID;
        this.cardId = cardId;
    }

    private long gamePlayerID;

    private String cardId;

}
