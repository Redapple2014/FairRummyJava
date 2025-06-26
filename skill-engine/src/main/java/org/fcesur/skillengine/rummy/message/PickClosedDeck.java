package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PickClosedDeck extends Message {
    private List<List<String>> groupCards;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param groupCards
     */
    public PickClosedDeck(long tableId, List<List<String>> groupCards) {
        super(1, MessageConstants.PICK_CLOSE_DECK, tableId);
        this.groupCards = groupCards;
    }

}
