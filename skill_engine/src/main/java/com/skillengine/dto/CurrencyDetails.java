package com.skillengine.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyDetails
{
	private BigDecimal depositBucket;
	private BigDecimal withdrawable;
	private BigDecimal nonWithdrawable;
	private long userId;
	private BigDecimal updatedDeposit;
	private BigDecimal updatedWithdrawable;
	private BigDecimal updatedNonWithdrawable;
	private int status;
}
