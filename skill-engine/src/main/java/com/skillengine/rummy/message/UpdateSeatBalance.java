package com.skillengine.rummy.message;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties( ignoreUnknown = true )
public class UpdateSeatBalance extends Message
{
	private BigDecimal seatBalance;
	private long seatPlayerId;
	private boolean isWinner;

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param seatBalance
	 * @param seatPlayerId
	 */
	public UpdateSeatBalance( long tableId, BigDecimal seatBalance, long seatPlayerId, boolean isWinner )
	{
		super( 1, MessageConstants.UPDATE_SEAT_BALANCE, tableId );
		this.seatBalance = seatBalance;
		this.seatPlayerId = seatPlayerId;
		this.isWinner = isWinner;
	}

}
