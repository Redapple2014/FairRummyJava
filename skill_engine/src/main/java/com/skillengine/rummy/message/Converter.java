package com.skillengine.rummy.message;

import java.util.ArrayList;
import java.util.List;

import com.skillengine.rummy.player.SeatPlayerInfo;
import com.skillengine.rummy.table.SeatInfo;

public class Converter
{

	public static List< SeatDetails > convertSeatsList( List< SeatInfo > seatInfos, long seatPlayerId, int playerScore )
	{

		List< SeatDetails > outSeatList = new ArrayList<>();

		for( int i = 0; i < seatInfos.size(); i++ )
		{
			SeatInfo seatInfo = seatInfos.get( i );
			SeatPlayerInfo player = seatInfo.getPlayer();
			if( player == null )
			{
				continue;
			}
			UserInfo userInfo = new UserInfo( -1, player.getUserId(), player.getUserName(), player.getAvatarId() );
			SeatDetails seatDetails = new SeatDetails( -1, seatInfo.getId(), seatInfo.getState(), userInfo, seatInfo.getSeatPlayerBalance(), playerScore );
			outSeatList.add( seatDetails );
		}
		return outSeatList;

	}
}
