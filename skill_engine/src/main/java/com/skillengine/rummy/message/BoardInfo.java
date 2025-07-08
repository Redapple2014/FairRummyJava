package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.BOARD_INFO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class BoardInfo extends Message
{
	private int maxPlayer;
	private int minBuyin;
	private int maxBuyin;
	private int noOfCards;
	private int gameNo;
	private int templateId;

	public BoardInfo( int maxPlayer, int minBuyin, int maxBuyin, long tableID, int noOfCards, int gameNo, int templateId )
	{
		super( 1, BOARD_INFO, tableID );
		this.maxPlayer = maxPlayer;
		this.minBuyin = minBuyin;
		this.maxBuyin = maxBuyin;
		this.noOfCards = noOfCards;
		this.gameNo = gameNo;
		this.templateId = templateId;
	}

	public int getMaxPlayer()
	{
		return maxPlayer;
	}

	public int getMinBuyin()
	{
		return minBuyin;
	}

	public int getMaxBuyin()
	{
		return maxBuyin;
	}

	public int getNoOfCards()
	{
		return noOfCards;
	}

	public int getGameNo()
	{
		return gameNo;
	}

	public int getTemplateId()
	{
		return templateId;
	}

}
