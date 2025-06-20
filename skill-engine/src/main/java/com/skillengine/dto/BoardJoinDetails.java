
 package com.skillengine.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardJoinDetails {
private long userId;
private BigDecimal txnAmount;
private BigDecimal bonusPercentage;
private long tableId;
private int templateId;
}
