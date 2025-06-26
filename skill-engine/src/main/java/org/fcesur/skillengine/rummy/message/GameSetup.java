package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSetup extends Message {

    private List<List<String>> handCard;
    private long dealerId;
    private String jokerCard;
    private List<String> openDeck;
    private float closedDeckSize;
    private long gameId;

    /**
     * Constructor
     *
     * @param tableId Table Id
     */
    public GameSetup(long tableId) {
        super(1, MessageConstants.GAME_SETUP, tableId);
    }
}
