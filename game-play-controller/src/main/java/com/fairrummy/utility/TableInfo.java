package com.fairrummy.utility;

import java.io.Serializable;
public class TableInfo implements Serializable
{
	private static final long serialVersionUID = 2850061435767767854L;

	private Long tableId = 0l;
	private Integer availableSeats;
	private Integer eligibility;
	private long gameStartTime = 0;
	private String engineIp;
	
	public TableInfo()
	{

	}

	public TableInfo( long tableId )
	{
		this.tableId = tableId;
	}

	public TableInfo( Long tableId, Integer availableSeats, Integer eligibility, long gameStartTime )
	{
		this.tableId = tableId;
		this.availableSeats = availableSeats;
		this.eligibility = eligibility;
		this.gameStartTime = gameStartTime;
	}

	public Long getTableId()
	{
		return tableId;
	}

	public void setTableId( Long tableId )
	{
		this.tableId = tableId;
	}

	public Integer getAvailableSeats()
	{
		return availableSeats;
	}

	public void setAvailableSeats( Integer availableSeats )
	{
		this.availableSeats = availableSeats;
	}

	public Integer getEligibility()
	{
		return eligibility;
	}

	public void setEligibility( Integer eligibility )
	{
		this.eligibility = eligibility;
	}

	/**
	 * @return the gameStartTime
	 */
	public long getGameStartTime()
	{
		return gameStartTime;
	}

	/**
	 * @param gameStartTime
	 *                the gameStartTime to set
	 */
	public void setGameStartTime( long gameStartTime )
	{
		this.gameStartTime = gameStartTime;
	}

	public String getEngineIp() {
		return engineIp;
	}

	public void setEngineIp(String engineIp) {
		this.engineIp = engineIp;
	}

	@Override
	public String toString() {
		return "TableInfo{" +
				"tableId=" + tableId +
				", availableSeats=" + availableSeats +
				", eligibility=" + eligibility +
				", gameStartTime=" + gameStartTime +
				", engineIp='" + engineIp + '\'' +
				'}';
	}
}
