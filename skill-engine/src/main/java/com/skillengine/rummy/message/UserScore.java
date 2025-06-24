package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class UserScore {

    private long playingPlayerId;
    private int score;
    private int status;
    private double txnAmt;
    private List<List<String>> cardIds;
}