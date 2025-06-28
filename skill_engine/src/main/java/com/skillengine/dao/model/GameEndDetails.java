package com.skillengine.dao.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameEndDetails
{
	private BigDecimal entryFee;
	private long userId;
	private BigDecimal rake;
	private int droppedOrNot;
	private int winnerOrNot;
	private int gamecnt;
	private BigDecimal winningAmt;
	private BigDecimal losingAmt;
	private int totalGames;
	private int totalWinningGames;
	private int totalDroppedGames;
	private BigDecimal totalWinningAmt;
	private BigDecimal totalLosingAmt;

	/**
	 * @param entryFee
	 * @param rake
	 * @param winningAmt
	 * @param losingAmt
	 * @param totalGames
	 * @param totalWinningGames
	 * @param totalDroppedGames
	 */
	public GameEndDetails( BigDecimal entryFee, BigDecimal rake, BigDecimal winningAmt, BigDecimal losingAmt, int totalGames, int totalWinningGames, int totalDroppedGames )
	{
		super();
		this.entryFee = entryFee;
		this.rake = rake;
		this.winningAmt = winningAmt;
		this.losingAmt = losingAmt;
		this.totalGames = totalGames;
		this.totalWinningGames = totalWinningGames;
		this.totalDroppedGames = totalDroppedGames;
	}

}
