package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlayerBootedOut extends Message {
    private long playingPlayerId;
    private int score;
    private int leavingType;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param playingPlayerId
     * @param score
     */
    public PlayerBootedOut(long tableId, long playingPlayerId, int score, int leavingType) {
        super(1, MessageConstants.BOOT_OUT, tableId);
        this.playingPlayerId = playingPlayerId;
        this.score = score;
        this.leavingType = leavingType;
    }

}
