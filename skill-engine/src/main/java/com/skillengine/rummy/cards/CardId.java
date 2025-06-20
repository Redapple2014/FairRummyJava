package com.skillengine.rummy.cards;

public class CardId implements Comparable
{
	public static final int ACE = 14;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	public static final int NINE = 9;
	public static final int TEN = 10;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int CLUBS = 1;
	public static final int DIAMONDS = 2;
	public static final int HEARTS = 3;
	public static final int SPADES = 4;
	public static final int INVALID_CARD1 = -1;
	public static final int INVALID_CARD2 = -2;
	public static final int MAX_RANK = ACE;
	public static final int FIRST_RANK = TWO;
	public static final int INVALID_SUIT1 = -1;
	public static final int INVALID_SUIT2 = -2;
	private int rank;
	private int suite;

	
	public CardId( int suite, int rank )
	{
		this.suite = suite;
		this.rank = rank;
		if( rank == 1 )
			rank = 14;
	}

	
	public CardId( String cid )
	{
		if( cid.equals( "j" ) )
		{
			this.rank = 0;
			this.suite = 0;
		}
		else
		{
			this.rank = Integer.parseInt( cid.substring( 0, cid.length() - 1 ) );
			if( rank == 1 )
				rank = 14;
			String s = cid.substring( cid.length() - 1 );
			if( s.equals( "c" ) )
				this.suite = CLUBS;
			else if( s.equals( "s" ) )
				this.suite = SPADES;
			else if( s.equals( "d" ) )
				this.suite = DIAMONDS;
			else if( s.equals( "h" ) )
				this.suite = HEARTS;
		}
	}

	
	public int getSuite()
	{
		return suite;
	}

	
	public int getRank()
	{
		return rank;
	}

	
	public CardId( CardId e )
	{
		this.suite = e.getSuite();
		this.rank = e.getRank();
		if( rank == 1 )
			rank = 14;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
		result = prime * result + suite;
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		CardId other = ( CardId ) obj;
		if( rank != other.rank )
			return false;
		if( suite != other.suite )
			return false;
		return true;
	}

	public int compareTo( Object o )
	{
		if( o == null )
		{
			return 1;
		}
		CardId card = ( CardId ) o;
		if( suite > card.getSuite() )
		{
			return 1;
		}
		else if( suite == card.getSuite() )
		{
			if( rank > card.rank )
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}

	
	public String toString()
	{
		String s;
		if( rank == 0 & suite == 0 )
		{
			s = "j";
		}
		else
		{
			if( rank == ACE )
				s = "1";
			else
				s = "" + this.rank;
			switch( suite )
			{
			case CLUBS:
				return s + "c";
			case HEARTS:
				return s + "h";
			case SPADES:
				return s + "s";
			case DIAMONDS:
				return s + "d";
			}
		}
		return s;
	}

	public boolean isValidCard()
	{
		return !( this.rank == INVALID_CARD1 || this.rank == INVALID_CARD2 || this.suite == INVALID_SUIT1 || this.suite == INVALID_SUIT2 );
	}
}