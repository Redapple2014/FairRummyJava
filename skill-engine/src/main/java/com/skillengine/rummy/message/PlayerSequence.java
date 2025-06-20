package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class PlayerSequence extends Message {

    /* ========== Constant(s) ========== */

    private static final int SERVICE_TYPE = 1;

    /* ========== Private member(s) ========== */

    private List<Long> playerIdList;

    /**
     * Constructor
     *
     * @param tableId      Table Id
     * @param playerIdList Player Id List
     */
    public PlayerSequence(long tableId, List<Long> playerIdList) {
        super(SERVICE_TYPE, MessageConstants.PLAYER_SEQ, tableId);

        this.playerIdList = playerIdList;
    }

}
