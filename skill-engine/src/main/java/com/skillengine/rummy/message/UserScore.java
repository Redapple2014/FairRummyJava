package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserScore {
    private long playingPlayerId;
    private int score;
    private List<List<String>> cardIds;
    private int status;
    private double txnAmt;
}
