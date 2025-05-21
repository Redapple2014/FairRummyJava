package com.skillengine.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameTemplates
{
	protected int id;
	protected int minBuyin;
	protected int maxBuyin;
	protected int status;
	protected int gid;
	protected String name;
	protected int minPlayer;
	protected int maxPlayer;
	private int noOfCards;
	private int gameStartTime;
	private int pointValue;
	private int noOfDeck;
	private int cardsPerPlayer;
	private int playerTurnTime;
	private double serviceFee;
	private int graceTime;
	private int dealsPerGame;
	private int variantType;

}
