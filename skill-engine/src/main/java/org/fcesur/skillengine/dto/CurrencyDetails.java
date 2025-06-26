package org.fcesur.skillengine.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyDetails {
    private BigDecimal depositBucket;
    private BigDecimal withdrawable;
    private BigDecimal nonWithdrawable;
    private long userId;
    private BigDecimal updatedDeposit;
    private BigDecimal updatedWithdrawable;
    private BigDecimal updatedNonWithdrawable;
    private int status;
}
