package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreUpdate extends Message {
    private String jokerCardId;
    private List<UserScore> userScore;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param jokerCardId
     * @param userScore
     */
    public ScoreUpdate(long tableId, String jokerCardId, List<UserScore> userScore) {
        super(1, MessageConstants.SCORE_UPDATE, tableId);
        this.jokerCardId = jokerCardId;
        this.userScore = userScore;
    }
}
