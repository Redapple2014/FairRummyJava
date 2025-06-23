package com.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder")
public class UserScore {

    private long playingPlayerId;
    private int score;
    private int status;
    private double txnAmt;
    private List<List<String>> cardIds;
}