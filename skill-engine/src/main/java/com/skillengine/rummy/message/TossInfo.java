package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TossInfo extends Message {

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param gameToss
     * @param tossWinnerId
     */
    public TossInfo(long tableId, List<GameToss> gameToss, long tossWinnerId) {
        super(1, MessageConstants.TOSS_INFO, tableId);
        this.gameToss = gameToss;
        this.tossWinnerId = tossWinnerId;
    }

    private List<GameToss> gameToss;
    private long tossWinnerId;

}
