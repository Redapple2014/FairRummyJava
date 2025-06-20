package com.skillengine.rummy.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.skillengine.rummy.cards.CardId;

public class RummyGameScore
{
	private List< List< CardId > > playerCard = null;
	private CardId jokerCard;
	private int pSeqCount = 0;
	private int seqCount = 0;
	private int setCount = 0;
	private int cardCount = 0;
	private List< CardId > pointCalCards = new ArrayList< CardId >();
	private List< CardId > allCards = new ArrayList< CardId >();
	private List< CardId > cardsExceptPseq = new ArrayList< CardId >();
	private int point = 0;
	private boolean all13Pure = false;

	List< String > usedCodes = new ArrayList< String >();

	/**
	 * Method exposed for calculating score.
	 */
	public int calculateScore( long mpid, boolean finishPlayer, int fullCount, boolean playerWithNoMoveInDealShowGame )
	{
		point = 0;
		try
		{
			for( int i = 0; i < playerCard.size(); i++ )
			{
				List< CardId > groupCard = playerCard.get( i );
				checkCards( groupCard );
				allCards.addAll( groupCard );
			}
			if( pSeqCount + seqCount > 1 && pSeqCount > 0 )
			{
				point = getScore( pointCalCards );
			}
			else
			{
				if( pSeqCount == 1 )
				{
					point = getScore( cardsExceptPseq );
				}
				else
				{
					point = getScore( allCards );
				}

			}
			// }
			if( all13Pure )
			{
				point = 0;
			}
			if( !finishPlayer && point == 0 )
			{
				point = 2;
			}
			if( point > fullCount )
			{
				point = fullCount;
			}
			if( !finishPlayer && playerWithNoMoveInDealShowGame && point > 2 )
			{
				if( point == 3 )
				{
					point = 2;
				}
				else
				{
					point = point / 2;
				}
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return point;
	}

	/**
	 * Method for calculating score of given card.
	 * 
	 */

	private int getScore( List< CardId > cards )
	{
		int score = 0;
		try
		{
			for( int i = 0; i < cards.size(); i++ )
			{
				CardId cid = cards.get( i );
				if( isJoker( cid ) )
				{
				}
				else
				{
					if( cid.getRank() == CardId.ACE )
					{
						score = score + 10;
					}
					else
					{
						if( cid.getRank() > 10 )
						{
							score = score + 10;
						}
						else
						{
							score = score + cid.getRank();
						}
					}
				}
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return score;
	}

	/**
	 * Method for checking given card group belong to pure
	 * sequence,sequence,set.
	 * 
	 */

	private void checkCards( List< CardId > cards )
	{
		try
		{
			if( isValidPSequence( cards ) )
			{
				if( cards.size() == 13 )
				{
					all13Pure = true;
				}
				pSeqCount++;
				cardCount = cardCount + cards.size();
			}
			else if( isValidSequence( cards ) )
			{
				seqCount++;
				cardCount = cardCount + cards.size();
				cardsExceptPseq.addAll( cards );
			}
			else if( isValidSet( cards ) )
			{
				setCount++;
				cardCount = cardCount + cards.size();
				cardsExceptPseq.addAll( cards );
			}
			else
			{
				pointCalCards.addAll( cards );
				cardsExceptPseq.addAll( cards );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	private boolean isAllJoker( List< CardId > set )
	{
		try
		{
			for( int i = 0; i < set.size(); i++ )
			{
				CardId card = set.get( i );
				if( !isJoker( card ) )
					return false;
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return true;
	}

	private boolean isJoker( CardId card )
	{

		if( jokerCard.getRank() == card.getRank() || card.getRank() == 0 || ( jokerCard.getRank() == 0 && card.getRank() == 14 ) )
		{
			return true;
		}
		return false;
	}

	public void setPlayerCard( List< List< CardId > > inputplayerCard )
	{
		playerCard = new ArrayList< List< CardId > >();
		if( inputplayerCard != null )
		{
			for( int i = 0; i < inputplayerCard.size(); i++ )
			{
				List< CardId > inputcard = inputplayerCard.get( i );
				List< CardId > cards = new ArrayList< CardId >();
				for( int j = 0; j < inputcard.size(); j++ )
				{
					CardId inputCard = inputcard.get( j );
					CardId newCard = new CardId( inputCard.getSuite(), inputCard.getRank() );
					cards.add( newCard );
				}
				playerCard.add( cards );
			}
		}
	}

	@SuppressWarnings( "unchecked" )
	private boolean isValidPSequence( List< CardId > pSeq )
	{
		boolean isValid = true;
		try
		{
			if( !minCardsPresent( pSeq ) )
			{
				return false;
			}
			List< CardId > duplicateCardList = new ArrayList< CardId >();
			for( int i = 0; i < pSeq.size(); i++ )
			{
				CardId card = pSeq.get( i );
				if( duplicateCardList.contains( card ) )
				{
					return false;
				}
				else
				{
					duplicateCardList.add( card );
				}

			}
			Collections.sort( pSeq );
			boolean lookOutForAce = false;
			int suite = pSeq.get( 0 ).getSuite();
			int rank = ( ( CardId ) pSeq.get( 0 ) ).getRank();
			if( rank == 2 )
				lookOutForAce = true;
			for( int i = 0; i < pSeq.size(); i++ )
			{
				CardId card = pSeq.get( i );
				if( !( card.getSuite() == suite && card.getRank() == rank ) )
				{
					if( !( lookOutForAce && card.getRank() == 14 && card.getSuite() == suite ) )
						isValid = false;
				}
				rank++;
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		return isValid;
	}

	@SuppressWarnings( "unchecked" )
	private boolean isValidSequence( List< CardId > seq )
	{
		try
		{
			if( seq.size() == 0 )
				return false;
			if( !minCardsPresent( seq ) )
			{
				return false;
			}
			List< CardId > duplicateCardList = new ArrayList< CardId >();
			for( int i = 0; i < seq.size(); i++ )
			{
				CardId card = seq.get( i );
				if( isJoker( card ) )
				{
					continue;
				}
				if( duplicateCardList.contains( card ) )
				{
					return false;
				}
				else
				{
					duplicateCardList.add( card );
				}

			}
			Collections.sort( seq );
			int numJokers = 0;
			int firstNonJokerCardRank = -1;
			int lastNonJokerCardRank = -1;
			int lastNonJokerNonAceCardRank = -1;
			int suite = -1;
			CardId card = null;
			for( int i = 0; i < seq.size(); i++ )
			{
				card = seq.get( i );
				if( isJoker( card ) )
				{
					numJokers++;
					continue;
				}
				// Not Joker
				if( card.getRank() != CardId.ACE )
					lastNonJokerNonAceCardRank = card.getRank();
				if( firstNonJokerCardRank == -1 )
				{
					firstNonJokerCardRank = card.getRank();
					suite = card.getSuite();
					continue;
				}
				if( card.getSuite() != suite )
				{
					return false;
				}
				lastNonJokerCardRank = card.getRank();

			}
			if( numJokers == seq.size() )
				return true;
			if( ( lastNonJokerCardRank - firstNonJokerCardRank ) < ( seq.size() ) )
				return true;
			if( lastNonJokerCardRank == CardId.ACE )
			{
				if( ( lastNonJokerNonAceCardRank - 1 ) < ( seq.size() ) )
					return true;
			}
			return false;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return false;
		}
	}

	private boolean isValidSet( List< CardId > cardList )
	{
		try
		{
			boolean isValid = true;
			if( cardList.size() == 0 )
				return true;
			if( isAllJoker( cardList ) )
				return true;
			if( !minCardsPresent( cardList ) )
			{
				return false;
			}
			int rank = 0;
			List< Integer > suite = new ArrayList< Integer >();
			Iterator< CardId > itor = cardList.iterator();
			boolean foundFirstNonJokerSetCard = false;
			while( itor.hasNext() )
			{
				CardId card = itor.next();
				if( isJoker( card ) )
				{
					continue;
				}
				if( !foundFirstNonJokerSetCard )
				{
					foundFirstNonJokerSetCard = true;
					rank = card.getRank();
				}
				if( !( card.getRank() == rank ) )
				{
					isValid = false;
				}
				if( suite.contains( card.getSuite() ) )
				{
					isValid = false;
				}
				else
				{
					suite.add( card.getSuite() );
				}
			}
			return isValid;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return false;
		}
	}

	public void setJokerCard( CardId jokerCard )
	{
		this.jokerCard = jokerCard;
	}

	private boolean minCardsPresent( List< CardId > cards )
	{
		if( cards.size() < 3 )
		{
			return false;
		}
		return true;
	}

}
