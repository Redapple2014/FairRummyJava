package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDeclaringDetails extends Message {
    private long winnerId;
    private List<PlayerDeclaringState> declaringStates;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param winnerId
     * @param declaringStates
     */
    public PlayerDeclaringDetails(long tableId, long winnerId, List<PlayerDeclaringState> declaringStates) {
        super(1, MessageConstants.DECLARE_DETAILS, tableId);
        this.winnerId = winnerId;
        this.declaringStates = declaringStates;
    }

}
