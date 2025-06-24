package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ScoreUpdate extends Message {

    private final String jokerCardId;
    private final List<UserScore> userScores;

    /**
     * Constructor
     *
     * @param tableId     Table id
     * @param jokerCardId Joker card id
     * @param userScores  User scores
     */
    public ScoreUpdate(long tableId, String jokerCardId, List<UserScore> userScores) {
        super(1, MessageConstants.SCORE_UPDATE, tableId);

        this.jokerCardId = jokerCardId;
        this.userScores = userScores;
    }
}
