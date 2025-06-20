package com.skillengine.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SettlementCurrencyInfo
{
	private BigDecimal companyRake;
	private Map< Long, BigDecimal > userCurrencyDetails;
	private int playersCnt;
	private long winnerId;
}
