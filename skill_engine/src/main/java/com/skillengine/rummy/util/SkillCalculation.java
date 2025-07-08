package com.skillengine.rummy.util;

import com.skillengine.dao.model.GameEndDetails;

public class SkillCalculation
{
	public static double calculateSkillScore( GameEndDetails details )
	{
		double nonDroppingGames = getNonDroppingGames( details );
		double netWinnings = getNetWinnings( details );
		double wagerEfficiency = getWagerEfficiency( details );
		double inverseDrop = getInverseDrop( details );
		return nonDroppingGames + netWinnings + wagerEfficiency + inverseDrop;
	}

	private static double getNonDroppingGames( GameEndDetails details )
	{
		try
		{
			return ( ( details.getTotalWinningGames() / ( details.getTotalGames() - details.getTotalDroppedGames() ) ) * 100 ) * 0.3;
		}
		catch( Exception e )
		{
			return 0;
		}
	}

	private static double getNetWinnings( GameEndDetails details )
	{
		try
		{
			return ( details.getWinningAmt().doubleValue() - details.getEntryFee().doubleValue() - details.getRake().doubleValue() ) * 0.3;
		}
		catch( Exception e )
		{
			return 0;
		}
	}

	private static double getWagerEfficiency( GameEndDetails details )
	{
		try
		{
			return ( details.getEntryFee().doubleValue() + details.getTotalLosingAmt().doubleValue() ) * 0.25;
		}
		catch( Exception e )
		{
			return 0;
		}
	}

	private static double getInverseDrop( GameEndDetails details )
	{
		try
		{
			return ( ( ( 1 - ( details.getTotalDroppedGames() ) / details.getTotalGames() ) ) * 100 ) * 0.15;
		}
		catch( Exception e )
		{
			return 0;
		}
	}
}
