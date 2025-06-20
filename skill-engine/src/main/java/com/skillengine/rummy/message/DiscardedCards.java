package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
public class DiscardedCards extends Message {
    private List<String> discardedCardId;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param discardedCardId
     */
    public DiscardedCards(long tableId, List<String> discardedCardId) {
        super(1, MessageConstants.DISCARDED_CARDS, tableId);
        this.discardedCardId = discardedCardId;
    }

}
