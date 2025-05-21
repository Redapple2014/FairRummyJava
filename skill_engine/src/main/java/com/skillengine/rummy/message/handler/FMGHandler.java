package com.skillengine.rummy.message.handler;

import com.skillengine.common.GameTemplates;
import com.skillengine.dao.TableDetailsDAO;
import com.skillengine.dao.model.TableDetails;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.rummy.globals.GameGlobals;
import com.skillengine.rummy.message.FMGRequest;
import com.skillengine.rummy.message.FMGResponse;
import com.skillengine.rummy.table.RummyBoard;
import com.skillengine.rummy.util.ActiveBoards;
import com.skillengine.sessions.PlayerSession;

public class FMGHandler implements MessageHandler< FMGRequest >
{

	private TableDetailsDAO dao = null;

	public FMGHandler( TableDetailsDAO dao )
	{
		this.dao = dao;
	}

	@Override
	public void handleMessage( PlayerSession session, FMGRequest message, long tableId )
	{
		if( tableId > 0 )
		{
			return;
		}
		GameTemplates gameTemplates = GameTemplates.builder().id( 10001 ).maxBuyin( 10000 ).maxPlayer( 2 ).minBuyin( 100 ).minPlayer( 2 ).noOfCards( 52 ).gameStartTime( 5000 )
				.cardsPerPlayer( 13 ).noOfDeck( 1 ).playerTurnTime( 5000 ).graceTime( 5000 ).pointValue( 1 ).variantType( 1 ).build();
		long chosenBoardId = ActiveBoards.getTable( gameTemplates.getId() );
		if( chosenBoardId > 0 )
		{
			FMGResponse fmgResponse = new FMGResponse( chosenBoardId );
			SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, fmgResponse );
			return;
		}
		TableDetails details = new TableDetails();
		details.setTemplateId( gameTemplates.getId() );
		details.setStatus( GameGlobals.STARTING );
		tableId = dao.insertTableDetails( details );
		RummyBoard board = new RummyBoard( tableId, gameTemplates );
		ActiveBoards.addTable( tableId, board );
		ActiveBoards.addTable( gameTemplates.getId(), tableId, System.currentTimeMillis() );
		FMGResponse fmgResponse = new FMGResponse( tableId );
		SkillEngineImpl.getInstance().getDispatcher().sendMessage( session, fmgResponse );
	}

}
