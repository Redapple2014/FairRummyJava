package com.skillengine.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExitDetails {
	private long userId;
	private BigDecimal nonWithdrawable;
	private BigDecimal withdrawable;
	private BigDecimal depositBucket;
	private long tableId;
}
