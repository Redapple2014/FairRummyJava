package com.skillengine.service;

import java.math.BigDecimal;

import com.skillengine.dto.BoardJoinDetails;
import com.skillengine.dto.CurrencyDetails;
import com.skillengine.dto.ExitDetails;

public class CurrencyServiceImpl implements CurrencyService
{

	@Override
	public CurrencyDetails debit( BoardJoinDetails boardJoinDetails )
	{
		// TODO Auto-generated method stub
		return CurrencyDetails.builder().depositBucket( new BigDecimal( "1000" ) ).withdrawable( new BigDecimal( "1000" ) ).status( 1 ).nonWithdrawable( BigDecimal.ZERO ).build();
	}

	@Override
	public CurrencyDetails credit( ExitDetails exitDetails )
	{
		// TODO Auto-generated method stub
		return CurrencyDetails.builder().depositBucket( new BigDecimal( "1000" ) ).withdrawable( new BigDecimal( "1000" ) ).status( 1 ).nonWithdrawable( BigDecimal.ZERO ).build();
	}

}
