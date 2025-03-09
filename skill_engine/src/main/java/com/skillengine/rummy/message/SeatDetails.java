package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.SEAT_INFO;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class SeatDetails extends Message
{
	private int id;
	private int state;
	private UserInfo userInfo;
	private BigDecimal seatBalance;

	public int getId()
	{
		return id;
	}

	public int getState()
	{
		return state;
	}

	public BigDecimal getSeatBalance()
	{
		return seatBalance;
	}

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public SeatDetails( long tableId, int id, int state, UserInfo userInfo, BigDecimal seatBalance )
	{
		super( 1, SEAT_INFO, tableId );
		this.id = id;
		this.state = state;
		this.userInfo = userInfo;
		this.seatBalance = seatBalance;
	}

}
