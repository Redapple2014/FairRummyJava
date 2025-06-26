package org.fcesur.skillengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExitDetails {
    private long userId;
    private BigDecimal nonWithdrawable;
    private BigDecimal withdrawable;
    private BigDecimal depositBucket;
    private long tableId;
}
