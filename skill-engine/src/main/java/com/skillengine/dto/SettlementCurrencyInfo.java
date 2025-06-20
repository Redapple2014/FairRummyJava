package com.skillengine.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
public class SettlementCurrencyInfo {
    private BigDecimal companyRake;
    private Map<Long, BigDecimal> userCurrencyDetails;
    private int playersCnt;
    private long winnerId;
}
