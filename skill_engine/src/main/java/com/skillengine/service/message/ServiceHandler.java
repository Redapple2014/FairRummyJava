package com.skillengine.service.message;

import com.skillengine.main.SkillEngineImpl;
import com.skillengine.message.parsers.Jackson;
import com.skillengine.rummy.message.BoardSetup;
import com.skillengine.rummy.message.Declare;
import com.skillengine.rummy.message.Discard;
import com.skillengine.rummy.message.DiscardCardReq;
import com.skillengine.rummy.message.Drop;
import com.skillengine.rummy.message.FMGRequest;
import com.skillengine.rummy.message.Finish;
import com.skillengine.rummy.message.LeaveBoard;
import com.skillengine.rummy.message.Message;
import com.skillengine.rummy.message.PickClosedDeck;
import com.skillengine.rummy.message.PickOpenDeck;
import com.skillengine.rummy.message.PlayerTableJoin;
import com.skillengine.rummy.message.TableCreation;
import com.skillengine.rummy.message.handler.DeclareHandler;
import com.skillengine.rummy.message.handler.DiscardCardHandler;
import com.skillengine.rummy.message.handler.DiscardHandler;
import com.skillengine.rummy.message.handler.DropHandler;
import com.skillengine.rummy.message.handler.FMGHandler;
import com.skillengine.rummy.message.handler.FinishHandler;
import com.skillengine.rummy.message.handler.LeaveBoardHandler;
import com.skillengine.rummy.message.handler.PickCloseHandler;
import com.skillengine.rummy.message.handler.PickOpenHandler;
import com.skillengine.rummy.message.handler.PlayerJoinHandler;
import com.skillengine.rummy.message.handler.SetupHandler;
import com.skillengine.rummy.message.handler.TableCreationHandler;
import com.skillengine.service.CurrencyService;
import com.skillengine.sessions.PlayerSession;

public class ServiceHandler
{
	private ServiceMessageDigester messageDigester;

	private CurrencyService currencyService;

	private Jackson jackson;

	public ServiceHandler( ServiceMessageDigester messageDigester, CurrencyService currencyService, Jackson jackson )
	{
		this.messageDigester = messageDigester;
		this.currencyService = currencyService;
		this.jackson = jackson;
	}

	public void parser( String gameSrvMsg )
	{
		ServiceMessage message = jackson.readValue( gameSrvMsg, ServiceMessage.class );
		PlayerSession playerSession = messageDigester.deserializeSession( message.getPlayerSession() );
		Message messages = messageDigester.deserialize( message.getGamePayload() );
		System.out.println( "messages" + message );
		switch( messages )
		{
		case PlayerTableJoin playerJoin -> new PlayerJoinHandler( currencyService ).handleMessage( playerSession, playerJoin, message.getReceiverId() );
		case BoardSetup boardSetup -> new SetupHandler().handleMessage( playerSession, boardSetup, message.getReceiverId() );
		case TableCreation tableCreate -> new TableCreationHandler( currencyService ).handleMessage( playerSession, tableCreate, message.getReceiverId() );
		case PickOpenDeck pickOpen -> new PickOpenHandler().handleMessage( playerSession, pickOpen, message.getReceiverId() );
		case PickClosedDeck PickClosedDeck -> new PickCloseHandler().handleMessage( playerSession, PickClosedDeck, message.getReceiverId() );
		case Drop drop -> new DropHandler().handleMessage( playerSession, drop, message.getReceiverId() );
		case Discard discard -> new DiscardHandler().handleMessage( playerSession, discard, message.getReceiverId() );
		case Declare declare -> new DeclareHandler().handleMessage( playerSession, declare, message.getReceiverId() );
		case LeaveBoard leave -> new LeaveBoardHandler().handleMessage( playerSession, leave, message.getReceiverId() );
		case Finish finish -> new FinishHandler().handleMessage( playerSession, finish, message.getReceiverId() );
		case DiscardCardReq discardCard -> new DiscardCardHandler().handleMessage( playerSession, discardCard, message.getReceiverId() );
		case FMGRequest fmgReq -> new FMGHandler( SkillEngineImpl.getInstance().getTableDetailsDAO() ).handleMessage( playerSession, fmgReq, message.getReceiverId() );
		default -> throw new IllegalArgumentException( "Unexpected value: " + messages );
		}
	}
}
