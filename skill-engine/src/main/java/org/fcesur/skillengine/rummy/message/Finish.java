package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Finish extends Message {
    private List<List<String>> groupCards;
    private String discardCardId;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param groupCards
     * @param discardCardId
     */
    public Finish(long tableId, List<List<String>> groupCards, String discardCardId) {
        super(1, MessageConstants.FINISH, tableId);
        this.groupCards = groupCards;
        this.discardCardId = discardCardId;
    }

}
