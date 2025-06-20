package com.skillengine.rummy.game;

import com.skillengine.rummy.cards.CardId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserHandInfo {
    private long userId;
    private String userName;
    private int avatarId;
    private BigDecimal depositBalance;
    private BigDecimal withdrawable;
    private BigDecimal nonWithdrawable;
    private int moveCount;
    private int turnCount;
    private List<List<CardId>> groupCards;
    private long declareStartTime;

    public int incrementMoveCount() {
        return ++moveCount;
    }

    public void incrementTurnCount() {
        ++turnCount;
    }
}
