package com.skillengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BoardJoinDetails {
    private long userId;
    private BigDecimal txnAmount;
    private BigDecimal bonusPercentage;
    private long tableId;
    private int templateId;
}
