package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Declare extends Message {
    private List<List<String>> cardsGrouping;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param cardsGrouping
     */
    public Declare(long tableId, List<List<String>> cardsGrouping) {
        super(1, MessageConstants.DECLARE, tableId);
        this.cardsGrouping = cardsGrouping;
    }

}
