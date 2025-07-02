package com.skillengine.rummy.message.handler;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.message.DealScoreCard;
import com.skillengine.rummy.message.DealScoreRequest;
import com.skillengine.rummy.table.Board;
import com.skillengine.rummy.table.DealsRummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class DealScoreHandler implements MessageHandler< DealScoreRequest >
{

	@Override
	public void handleMessage( PlayerSession session, DealScoreRequest message, long tableId )
	{
		try
		{
			if( tableId <= 0 )
			{
				return;
			}
			Board board = ActiveBoards.getTable( tableId );
			if( board == null )
			{
				return;
			}
			if( board.getGameTemplates().getVariantType() != VariantTypes.DEALS_RUMMY )
			{
				return;
			}
			if( board instanceof DealsRummyBoard )
			{
				DealsRummyBoard dealsRummyBoard = ( DealsRummyBoard ) board;
				DealScoreCard dealScoreCard = dealsRummyBoard.generateScoreCard();
				SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, dealScoreCard );

			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}
