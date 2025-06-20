package com.skillengine.rummy.settlement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.skillengine.dto.PointSettlementInfo;
import com.skillengine.dto.SettlementCurrencyInfo;

public class PointsSettlement implements Settlement
{

	@Override
	public SettlementCurrencyInfo settlement( PointSettlementInfo pointSettlementInfo )
	{

		Map< Long, Integer > playerScore = pointSettlementInfo.getPlayerVsScore();
		long winnerId = pointSettlementInfo.getWinnerId();
		double totalPointsWonByUser = 0;
		Set< Long > playingPlayers = playerScore.keySet();
		Map< Long, BigDecimal > losingAmt = new HashMap<>();
		for( Long plId : playingPlayers )
		{
			if( plId.longValue() == winnerId )
			{
				continue;
			}
			int loserScore = playerScore.get( plId );
			totalPointsWonByUser = totalPointsWonByUser + ( loserScore * pointSettlementInfo.getPointValue() );
			losingAmt.put( plId, new BigDecimal( loserScore * pointSettlementInfo.getPointValue() ) );
		}
		double companyRake = totalPointsWonByUser * ( pointSettlementInfo.getServiceFee() / 100 );
		double updatedWinningAmt = totalPointsWonByUser - companyRake;
		losingAmt.put( winnerId, new BigDecimal( updatedWinningAmt ) );
		return SettlementCurrencyInfo.builder().companyRake( new BigDecimal( companyRake ) ).playersCnt( playerScore.size() ).userCurrencyDetails( losingAmt ).winnerId( winnerId ).build();
	}

}
