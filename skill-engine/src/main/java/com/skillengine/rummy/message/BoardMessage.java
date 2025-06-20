package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.skillengine.rummy.message.MessageConstants.BOARD_INFO;

/**
 * Board Info
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardMessage extends Message {

    public static final int SERVICE_TYPE = 1;

    private int maxPlayers;
    private int minBuyIn;
    private int maxBuyIn;
    private int numCards;
    private int gameNo;
    private int templateId;

    /**
     * Constructor
     *
     * @param maxPlayers Maximum number of players
     * @param minBuyIn   Minimum buy in
     * @param maxBuyIn   Maximum buy in
     * @param tableID    Table ID
     * @param numCards   Number of cards
     * @param gameNo     Game number
     * @param templateId Template ID
     */
    public BoardMessage(int maxPlayers, int minBuyIn, int maxBuyIn, long tableID, int numCards, int gameNo, int templateId) {
        super(SERVICE_TYPE, BOARD_INFO, tableID);

        this.maxPlayers = maxPlayers;
        this.minBuyIn = minBuyIn;
        this.maxBuyIn = maxBuyIn;
        this.numCards = numCards;
        this.gameNo = gameNo;
        this.templateId = templateId;
    }
}
