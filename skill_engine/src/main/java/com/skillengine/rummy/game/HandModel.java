package com.skillengine.rummy.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skillengine.rummy.cards.CardDeck;
import com.skillengine.rummy.cards.CardId;
import com.skillengine.rummy.player.PlayerInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandModel
{
	protected CardDeck openCardStack;
	protected CardDeck closedCardStack;
	private CardId finishedCard = null;
	protected CardId jokerCard = null;
	protected int cardsInDeal = 13;
	protected Map< Long, CardDeck > playersDeck = new HashMap< Long, CardDeck >();
	private Map< Long, Integer > playersMissedMove = new HashMap< Long, Integer >();
	public static final int FORFEIT_ON_CONSEC_MISSED_MOVES = 3;
	private boolean openJokerPick = true;
	private CardId pickedCard;
	ArrayList< CardId > jokers = new ArrayList< CardId >();
	private Map< Long, Integer > playersMoveCount = new HashMap< Long, Integer >();
	private Long finishMatchPlayerId = 0L;
	private List< String > discardedCardIds = new ArrayList<>();
	private Map< Long, List< List< String > > > playerGroupedHandCards = new HashMap<>();

	public HandModel( List< PlayerInfo > orderedPlayer, int noOfjoker, int noOfDeck, int cardToDeal )
	{

		try
		{
			if( cardToDeal > 0 )
				cardsInDeal = cardToDeal;
			closedCardStack = new CardDeck( noOfDeck, noOfjoker );
			closedCardStack.shuffle();
			for( int j = 0; j < cardToDeal; j++ )
			{
				for( int i = 0; i < orderedPlayer.size(); i++ )
				{
					log.info( "Ordered Player {}", orderedPlayer.get( i ) );
					PlayerInfo player = orderedPlayer.get( i );
					long playerId = player.getUserId();
					CardDeck playerCardDeck = playersDeck.get( playerId );
					if( playerCardDeck == null )
					{
						ArrayList< CardId > card = new ArrayList< CardId >();
						playerCardDeck = new CardDeck( card );
					}
					playerCardDeck.addCard( closedCardStack.getTopCard() );
					playersDeck.put( playerId, playerCardDeck );
				}
			}
			jokerCard = closedCardStack.getTopCard();
			openCardStack = new CardDeck( closedCardStack.getCards( 1 ) );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	public CardId pickOpenCard( long mPlayerId )
	{
		CardId topOpenCard = null;
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			if( deck.getSize() == cardsInDeal )
			{
				topOpenCard = openCardStack.getTopCard();
				deck.addCard( topOpenCard );
				increaseMoveCount( mPlayerId );
				openJokerPick = false;
				pickedCard = topOpenCard;
				playersMissedMove.put( mPlayerId, 0 );
				if( !discardedCardIds.isEmpty() )
				{
					String topCard = discardedCardIds.get( discardedCardIds.size() - 1 );
					CardId topCardId = new CardId( topCard );
					if( topCardId.equals( topOpenCard ) )
					{
						discardedCardIds.remove( discardedCardIds.size() - 1 );
					}
				}
			}
			else
			{
				log.info( "Alert in pickopen user does not have 13 card " + mPlayerId );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return topOpenCard;
	}

	public boolean canPickOpenCard()
	{
		boolean succ = false;
		CardId openCard = openCardStack.veiwTopCard();
		if( openCard.getRank() == jokerCard.getRank() || openCard.getRank() == 0 || ( jokerCard.getRank() == 0 && openCard.getRank() == 14 ) )
		{
			succ = openJokerPick;
		}
		else
		{
			succ = true;
		}
		return succ;
	}

	private void increaseMoveCount( Long mPlayerId )
	{
		try
		{
			int count = 1;
			if( playersMoveCount.containsKey( mPlayerId ) )
			{
				count = playersMoveCount.get( mPlayerId );
			}
			count++;
			playersMoveCount.put( mPlayerId, count );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	public List< List< CardId > > checkGroupedCardForSetCards( long mPlayerId, List< List< String > > array, CardId lastPickedOrDiscarded )
	{
		List< List< CardId > > groupedCard = new ArrayList< List< CardId > >();
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			List< CardId > handcardArray = deck.getCards();
			List< CardId > playersTotalCard = new ArrayList< CardId >();
			for( int i = 0; i < array.size(); i++ )
			{
				List< String > groupCard = array.get( i );
				List< CardId > groupCardIds = new ArrayList< CardId >();
				for( int j = 0; j < groupCard.size(); j++ )
				{
					String cardstr = ( String ) groupCard.get( j );
					CardId id = new CardId( cardstr );
					groupCardIds.add( id );
					playersTotalCard.add( id );
				}
				groupedCard.add( groupCardIds );
			}
			if( handcardArray.size() != playersTotalCard.size() )
			{
				int cardDiff = handcardArray.size() > playersTotalCard.size() ? handcardArray.size() - playersTotalCard.size() : playersTotalCard.size() - handcardArray.size();
				if( cardDiff > 1 )
				{
					log.info( "Alert Group Card Not Matching PlayerId : " + mPlayerId + " handcardArray.size : " + handcardArray + " playersTotalCard.size : " + playersTotalCard
							+ "Diff : " + cardDiff );
					return null;
				}
				if( lastPickedOrDiscarded != null )
				{
					log.info( "PlayerId : " + mPlayerId + " handcardArray.size : " + handcardArray.size() + " playersTotalCard.size : " + playersTotalCard.size()
							+ " lastPicked/DiscardedCard : " + lastPickedOrDiscarded );
					if( handcardArray.size() > playersTotalCard.size() )
					{
						playersTotalCard.add( lastPickedOrDiscarded );
					}
					else
					{
						playersTotalCard.remove( lastPickedOrDiscarded );
					}
				}
				else
				{
					return null;
				}
			}
			for( int i = 0; i < handcardArray.size(); i++ )
			{
				CardId handCard = handcardArray.get( i );
				playersTotalCard.remove( handCard );
			}
			if( !playersTotalCard.isEmpty() )
			{
				log.info( "Alert! Cards are not same for user : " + mPlayerId + " extracards : " + playersTotalCard );
				return null;
			}
		}
		catch( Exception ex )
		{
			log.error( "Match Player Id:" + mPlayerId + "-" + ex.getMessage(), ex );
			return null;
		}

		return groupedCard;
	}

	public List< List< String > > getPlayerHandString( long mPlayerId )
	{
		try
		{
			List< List< String > > mainCardStr = new ArrayList< List< String > >();
			CardDeck deck = playersDeck.get( mPlayerId );
			List< String > groupCardStr = deck.getAllCards();
			mainCardStr.add( groupCardStr );
			return mainCardStr;
		}
		catch( Exception ex )
		{
			log.error( "Match Player Id:" + mPlayerId + "-" + ex.getMessage(), ex );
			return null;
		}
	}

	public int getClosedDeckSize()
	{
		return closedCardStack.getSize();
	}

	public CardId getJokerCard()
	{
		return jokerCard;
	}

	public List< String > getOpenStackString()
	{
		return openCardStack.getAllCards();
	}

	public CardId moveMissed( long mPlayerId )
	{
		CardId card = null;
		try
		{
			if( pickedCard != null )
			{
				CardDeck deck = playersDeck.get( mPlayerId );
				if( deck.getSize() == cardsInDeal + 1 && deck.contains( pickedCard ) )
				{
					deck.removeCard( pickedCard );
					openCardStack.addCard( pickedCard );
					card = new CardId( pickedCard.getSuite(), pickedCard.getRank() );
					pickedCard = null;
				}
			}
			else
			{
				int missedMove = 0;
				if( playersMissedMove.containsKey( mPlayerId ) )
				{
					missedMove = playersMissedMove.get( mPlayerId );
				}
				missedMove++;
				playersMissedMove.put( mPlayerId, missedMove );
			}
			increaseMoveCount( mPlayerId );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return card;
	}

	public boolean discard( long mPlayerId, CardId discardCard )
	{
		boolean succ = false;
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			if( deck.getSize() == cardsInDeal + 1 && deck.contains( discardCard ) )
			{
				deck.removeCard( discardCard );
				openCardStack.addCard( discardCard );
				succ = true;
				pickedCard = null;
				if( !discardedCardIds.isEmpty() )
				{
					String topCard = discardedCardIds.get( discardedCardIds.size() - 1 );
					CardId topCardId = new CardId( topCard );
					if( topCardId.equals( discardCard ) )
					{
						return succ;
					}
				}
				discardedCardIds.add( discardCard.toString() );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return succ;
	}

	public int getMissedMoveCount( long mPlayerId )
	{
		int missedMove = 0;
		if( playersMissedMove.containsKey( mPlayerId ) )
		{
			missedMove = playersMissedMove.get( mPlayerId );
		}
		return missedMove;
	}

	public boolean resuffle()
	{
		boolean succ = false;
		try
		{
			if( closedCardStack.getSize() == 0 )
			{
				CardId openCard = openCardStack.getTopCard();
				int size = openCardStack.getSize();
				closedCardStack.addCards( openCardStack.getCards( size ) );
				closedCardStack.shuffle();
				openCardStack.addCard( openCard );
				succ = true;
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return succ;
	}

	public boolean alreadyPickedCard( long mPlayerId )
	{
		boolean succ = false;
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			if( deck.getSize() == cardsInDeal + 1 )
			{
				succ = true;
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return succ;
	}

	public List< CardId > getPlayerHand( long mPlayerId )
	{
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			List< CardId > cardList = deck.getCards();
			return cardList;
		}
		catch( Exception ex )
		{
			log.error( "Match Player Id:" + mPlayerId + "-" + ex.getMessage(), ex );
			return null;
		}
	}

	public CardId pickClosedCard( long mPlayerId )
	{
		CardId topClosedCard = null;
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			if( deck.getSize() == cardsInDeal )
			{
				topClosedCard = closedCardStack.getTopCard();
				deck.addCard( topClosedCard );
				increaseMoveCount( mPlayerId );
				openJokerPick = false;
				pickedCard = topClosedCard;
				playersMissedMove.put( mPlayerId, 0 );
			}
			else
			{
				log.info( "pickclose.User does not have 13 card.Its not turn of " + mPlayerId );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return topClosedCard;
	}

	public List< List< CardId > > getGamePlayerHandCards( long mPlayerId )
	{
		try
		{
			List< List< CardId > > mainCardStr = new ArrayList< List< CardId > >();
			CardDeck deck = playersDeck.get( mPlayerId );
			List< CardId > cardList = deck.getCards();
			Collections.sort( cardList );
			mainCardStr.add( cardList );
			return mainCardStr;
		}
		catch( Exception ex )
		{
			log.error( "Match Player Id:" + mPlayerId + "-" + ex.getMessage(), ex );
			return null;
		}
	}

	public boolean finish( long mPlayerId, CardId finishCard )
	{
		boolean succ = false;
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			if( deck.getSize() == cardsInDeal + 1 && deck.contains( finishCard ) )
			{
				deck.removeCard( finishCard );
				finishedCard = finishCard;
				// finishMatchPlayerId = mPlayerId;
				pickedCard = null;
				succ = true;
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return succ;
	}

	public boolean addFcardToOpenDeck()
	{
		boolean succ = false;
		try
		{
			openCardStack.addCard( finishedCard );
			succ = true;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return succ;
	}

	public CardId getFinishedCard()
	{
		return finishedCard;
	}

	public List< List< String > > getPlayerGroupedCardStr( long mPlayerId, List< List< CardId > > main )
	{
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			List< List< String > > mainCardStr = new ArrayList< List< String > >();
			List< CardId > handcardArray = deck.getCards();
			List< CardId > playersTotalCard = new ArrayList< CardId >();

			for( int i = 0; i < main.size(); i++ )
			{
				List< CardId > groupCard = main.get( i );
				List< String > groupCardStr = new ArrayList< String >();
				for( int j = 0; j < groupCard.size(); j++ )
				{
					CardId id = groupCard.get( j );
					groupCardStr.add( id.toString() );
					playersTotalCard.add( id );
				}
				mainCardStr.add( groupCardStr );
			}
			if( handcardArray.size() != playersTotalCard.size() )
			{
				return null;
			}
			for( int i = 0; i < handcardArray.size(); i++ )
			{
				CardId handCard = handcardArray.get( i );
				playersTotalCard.remove( handCard );

			}
			if( !playersTotalCard.isEmpty() )
			{
				return null;
			}
			return mainCardStr;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return null;
		}
	}

	public List< List< CardId > > sortGamePlayerHandCardss( final long playerId )
	{
		List< CardId > handCards = new ArrayList< CardId >( getPlayerHand( playerId ) );
		List< List< CardId > > sortedCards = new ArrayList< List< CardId > >();
		Collections.sort( handCards );
		CardId cardIdTmp = handCards.get( 0 );
		List< CardId > groupCards = new ArrayList< CardId >();
		for( CardId handCard : handCards )
		{
			if( cardIdTmp.getSuite() == handCard.getSuite() )
			{
				groupCards.add( handCard );
			}
			else
			{
				sortedCards.add( new ArrayList< CardId >( groupCards ) );
				groupCards.clear();
				groupCards.add( handCard );
				cardIdTmp = handCard;
			}
		}
		if( groupCards.size() > 0 )
		{
			sortedCards.add( new ArrayList< CardId >( groupCards ) );
		}
		return sortedCards;
	}

	public CardDeck getPlayersDeck( Long playerid )
	{
		return playersDeck.get( playerid );
	}

	public List< List< CardId > > checkGroupedCard( long mPlayerId, List< List< String > > array )
	{
		List< List< CardId > > groupedCard = new ArrayList< List< CardId > >();
		try
		{
			CardDeck deck = playersDeck.get( mPlayerId );
			List< CardId > handcardArray = deck.getCards();
			List< CardId > playersTotalCard = new ArrayList< CardId >();
			for( int i = 0; i < array.size(); i++ )
			{
				List< String > groupCard = array.get( i );
				List< CardId > groupCardIds = new ArrayList< CardId >();
				for( int j = 0; j < groupCard.size(); j++ )
				{
					String cardstr = ( String ) groupCard.get( j );
					CardId id = new CardId( cardstr );
					groupCardIds.add( id );
					playersTotalCard.add( id );
				}
				groupedCard.add( groupCardIds );
			}
			if( handcardArray.size() != playersTotalCard.size() || ( playersTotalCard.size() == 14 ) )
			{
				return null;
			}
			for( int i = 0; i < handcardArray.size(); i++ )
			{
				CardId handCard = handcardArray.get( i );
				playersTotalCard.remove( handCard );
			}
			if( !playersTotalCard.isEmpty() )
			{
				log.info( "Cards are not same for Player : " + mPlayerId + " extra : " + playersTotalCard );
				return null;
			}
		}
		catch( Exception ex )
		{
			log.error( "Match User Id:" + mPlayerId + "-" + ex.getMessage(), ex );
			return null;
		}

		return groupedCard;
	}

	public void removeFinishMatchPlayerId()
	{
		this.finishMatchPlayerId = 0L;
	}

	public List< String > getDiscardCards()
	{
		return discardedCardIds;
	}

	public List< List< String > > addGroupedHandCard( long userId, List< List< String > > groupedCards )
	{
		playerGroupedHandCards.put( userId, groupedCards );
		return groupedCards;
	}

	public List< List< String > > getGroupHandCard( long userId )
	{
		return playerGroupedHandCards.get( userId );
	}
	
	public boolean isGroupCardAvail(long userId)
	{
		return playerGroupedHandCards.containsKey( userId );
	}

}
