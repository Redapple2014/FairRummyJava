package com.skillengine.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PointSettlementInfo
{
	private Map< Long, Integer > playerVsScore;
	private long winnerId;
	private double serviceFee;
	private double pointValue;
}
