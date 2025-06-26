package org.fcesur.skillengine.rummy.message;

import lombok.Getter;

@Getter
public class PlayerTurnIntimation extends Message {
    private long playingPlayerId;
    private long turnTimer;
    private long graceTimer;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param playingPlayerId
     * @param turnTimer
     */
    public PlayerTurnIntimation(long tableId, long playingPlayerId, long turnTimer, long graceTimer) {
        super(1, MessageConstants.PLAYER_TURN, tableId);
        this.playingPlayerId = playingPlayerId;
        this.turnTimer = turnTimer;
        this.graceTimer = graceTimer;
    }

}
