package com.skillengine.rummy.cards;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck
{

	public final Integer TOTAL_DECK_SIZE = 52;
	public final int MAX_SUITE = 4;
	public final int MIN_SUITE = 1;
	public final int MAX_RANK = 14;
	public final int FIRST_RANK = 2;
	private List< CardId > cards = null;
	private static SecureRandom rand = null;
	static
	{
		try
		{
			rand = SecureRandom.getInstance( "SHA1PRNG", "SUN" );
		}
		catch( Exception ex )
		{
			rand = new SecureRandom();
			ex.printStackTrace();
		}
	}

	public CardDeck( int noOfJoker )
	{
		cards = new ArrayList< CardId >();
		for( int i = MIN_SUITE; i <= MAX_SUITE; i++ )
		{
			for( int j = FIRST_RANK; j <= MAX_RANK; j++ )
			{
				cards.add( new CardId( i, j ) );
			}
		}
		for( int i = 1; i > noOfJoker; i++ )
		{
			cards.add( new CardId( 0, 0 ) );
		}
	}

	// Pack of 52 Cards
	public CardDeck( int numdecks, int noOfJoker )
	{
		cards = new ArrayList< CardId >();
		for( int x = 0; x < numdecks; x++ )
		{
			for( int i = MIN_SUITE; i <= MAX_SUITE; i++ )
			{
				for( int j = FIRST_RANK; j <= MAX_RANK; j++ )
				{
					cards.add( new CardId( i, j ) );
				}
			}
		}
		for( int i = 0; i < noOfJoker; i++ )
		{
			cards.add( new CardId( 0, 0 ) );
		}

	}

	public CardDeck( int numdecks, int noOfJoker, int minRank, int maxRank )
	{
		cards = new ArrayList< CardId >();
		for( int x = 0; x < numdecks; x++ )
		{
			for( int i = MIN_SUITE; i <= MAX_SUITE; i++ )
			{
				for( int j = minRank; j <= maxRank; j++ )
				{
					cards.add( new CardId( i, j ) );
				}
			}
		}
		for( int i = 0; i < noOfJoker; i++ )
		{
			cards.add( new CardId( 0, 0 ) );
		}

	}

	public CardDeck( List< CardId > selectedCardArray )
	{
		if( cards != null )
		{
			cards.clear();
		}
		else
		{
			cards = new ArrayList< CardId >();
		}
		cards.addAll( selectedCardArray );
	}

	public void addCard( CardId b )
	{
		if( b != null )
			cards.add( b );
	}

	public void addCards( List< CardId > d )
	{
		if( d != null )
			cards.addAll( d );
	}

	public void removeCard( CardId a )
	{
		if( cards.contains( a ) )
		{
			cards.remove( a );
		}
	}

	public CardId getTopCard()
	{
		CardId topCard = cards.remove( cards.size() - 1 );
		return topCard;
	}

	public CardId veiwTopCard()
	{
		CardId topCard = null;
		if( cards.size() > 0 )
			topCard = cards.get( cards.size() - 1 );
		return topCard;
	}

	public List< CardId > getCards( int number )
	{
		List< CardId > reqCards = new ArrayList< CardId >();
		for( int i = 0; i < number; i++ )
		{
			CardId topCard = cards.remove( cards.size() - 1 );
			reqCards.add( topCard );
		}
		return reqCards;
	}

	public CardId popCardAt( int position )
	{
		CardId card = cards.get( position );
		return card;
	}

	public void shuffle()
	{
		try
		{
			int size = cards.size();
			for( int i = size - 1; i > 0; i-- )
			{
				int index = rand.nextInt( i + 1 );
				CardId temp = cards.set( index, cards.get( i ) );
				cards.set( i, temp );

			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}

	}

	@SuppressWarnings( "unchecked" )
	public void sortTheCards()
	{
		Collections.sort( cards );
	}

	public List< String > getAllCards()
	{
		List< String > l = new ArrayList< String >();
		for( int i = 0; i < cards.size(); i++ )
		{
			CardId id = cards.get( i );
			l.add( id.toString() );
		}
		return l;
	}

	public List< CardId > getAllCardId()
	{
		List< CardId > l = new ArrayList< CardId >();
		for( int i = 0; i < cards.size(); i++ )
		{
			CardId id = cards.get( i );
			l.add( id );
		}
		return l;
	}

	public boolean isShuffled()
	{
		return true;
	}

	public int getSize()
	{
		return cards.size();
	}

	public boolean contains( CardId id )
	{
		boolean succ = false;
		if( id != null )
			succ = cards.contains( id );
		return succ;
	}

	public List< CardId > getCards()
	{
		List< CardId > l = new ArrayList< CardId >();
		for( int i = 0; i < cards.size(); i++ )
		{
			CardId id = cards.get( i );
			l.add( id );
		}
		return l;
	}

	public CardId getLowerCard()
	{
		int idRank = 16;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( idRank > cardId.getRank() )
			{
				idRank = cardId.getRank();
				idSuite = cardId.getSuite();
			}
		}
		return new CardId( idSuite, idRank );
	}

	public CardId getHigherCard()
	{
		int idRank = 0;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( idRank < cardId.getRank() )
			{
				idRank = cardId.getRank();
				idSuite = cardId.getSuite();
			}
		}
		return new CardId( idSuite, idRank );
	}

	public CardId getHigherCardWithSuit( int curSuit )
	{
		CardId tCardId = null;
		int idRank = 0;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( idRank < cardId.getRank() )
			{
				if( cardId.getSuite() == curSuit )
				{
					idRank = cardId.getRank();
					idSuite = cardId.getSuite();
				}
			}
		}
		if( idSuite > 0 )
		{
			tCardId = new CardId( idSuite, idRank );
		}
		return tCardId;
	}

	public CardId getLowerCardWithSuit( int curSuit )
	{
		CardId tCardId = null;
		int idRank = 16;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( idRank > cardId.getRank() )
			{
				if( cardId.getSuite() == curSuit )
				{
					idRank = cardId.getRank();
					idSuite = cardId.getSuite();
				}
			}
		}
		if( idSuite > 0 )
		{
			tCardId = new CardId( idSuite, idRank );
		}
		return tCardId;
	}

	public CardId getHigherCardWithTrump( int trmpSuit )
	{
		CardId tCardId = null;
		int idRank = 0;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( idRank < cardId.getRank() )
			{
				if( cardId.getSuite() == trmpSuit )
				{
					idRank = cardId.getRank();
					idSuite = cardId.getSuite();
				}
			}
		}
		if( idSuite > 0 )
		{
			tCardId = new CardId( idSuite, idRank );
		}
		return tCardId;
	}

	public CardId getLowerCardWithOutTrump( int trumpSuit )
	{
		CardId tCardId = null;
		int idRank = 16;
		int idSuite = 0;
		for( CardId cardId : cards )
		{
			if( cardId.getSuite() != trumpSuit )
			{
				if( idRank > cardId.getRank() )
				{
					idRank = cardId.getRank();
					idSuite = cardId.getSuite();
				}
			}
		}
		if( idSuite > 0 )
		{
			tCardId = new CardId( idSuite, idRank );
		}
		return tCardId;
	}

}
