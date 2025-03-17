package com.skillengine.service.message;

import com.skillengine.message.parsers.Jackson;
import com.skillengine.rummy.message.BoardSetup;
import com.skillengine.rummy.message.Declare;
import com.skillengine.rummy.message.Discard;
import com.skillengine.rummy.message.Drop;
import com.skillengine.rummy.message.Finish;
import com.skillengine.rummy.message.Message;
import com.skillengine.rummy.message.MessageConstants;
import com.skillengine.rummy.message.PickClosedDeck;
import com.skillengine.rummy.message.PickOpenDeck;
import com.skillengine.rummy.message.PlayerTableJoin;
import com.skillengine.rummy.message.TableCreation;
import com.skillengine.sessions.PlayerSession;

public class ServiceMessageDigester
{
	private Jackson jackson;

	public ServiceMessageDigester( Jackson jackson )
	{
		this.jackson = jackson;
	}

	public PlayerSession deserializeSession( String userSession )
	{
		return jackson.readValue( userSession, PlayerSession.class );
	}

	public Message deserialize( String payload )
	{
		String msgType = jackson.getMsgType( payload );
		switch( msgType )
		{
		case MessageConstants.PLAYER_TABLE_JOIN:
			return jackson.readValue( payload, PlayerTableJoin.class );
		case MessageConstants.BOARD_SETUP:
			return jackson.readValue( payload, BoardSetup.class );
		case MessageConstants.TABLE_CREATE:
			return jackson.readValue( payload, TableCreation.class );
		case MessageConstants.PICK_CLOSE_DECK:
			return jackson.readValue( payload, PickClosedDeck.class );
		case MessageConstants.PICK_OPEN_DECK:
			return jackson.readValue( payload, PickOpenDeck.class );
		case MessageConstants.DROP:
			return jackson.readValue( payload, Drop.class );
		case MessageConstants.FINISH:
			return jackson.readValue( payload, Finish.class );
		case MessageConstants.DISCARD:
			return jackson.readValue( payload, Discard.class );
		case MessageConstants.DECLARE:
			return jackson.readValue( payload, Declare.class );
		default:
			return null;

		}
	}

}
