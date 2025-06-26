package org.fcesur.skillengine.service;

import org.fcesur.skillengine.dto.BoardJoinDetails;
import org.fcesur.skillengine.dto.CurrencyDetails;
import org.fcesur.skillengine.dto.ExitDetails;

import java.math.BigDecimal;

public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public CurrencyDetails debit(BoardJoinDetails boardJoinDetails) {
        // TODO Auto-generated method stub
        return CurrencyDetails.builder().depositBucket(new BigDecimal("1000")).withdrawable(new BigDecimal("1000")).status(1).nonWithdrawable(BigDecimal.ZERO).build();
    }

    @Override
    public CurrencyDetails credit(ExitDetails exitDetails) {
        // TODO Auto-generated method stub
        return CurrencyDetails.builder().depositBucket(new BigDecimal("1000")).withdrawable(new BigDecimal("1000")).status(1).nonWithdrawable(BigDecimal.ZERO).build();
    }

}
