package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResetCards extends Message {
    private List<List<String>> cardIds;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param cardIds
     */
    public ResetCards(long tableId, List<List<String>> cardIds) {
        super(1, MessageConstants.RESET_CARDS, tableId);
        this.cardIds = cardIds;
    }

}
