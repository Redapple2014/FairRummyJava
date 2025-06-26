package org.fcesur.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetHandCards extends Message {

    private List<List<String>> cards;

    /**
     * Constructor
     *
     * @param tableId          Table Id
     * @param groupedHandCards Grouped Hand Cards
     */
    public SetHandCards(long tableId, List<List<String>> groupedHandCards) {
        super(1, MessageConstants.SET_HAND_CARDS, tableId);
        this.cards = groupedHandCards;
    }
}
