package com.skillengine.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class PointSettlementInfo {
    private Map<Long, Integer> playerVsScore;
    private long winnerId;
    private double serviceFee;
    private double pointValue;
}
