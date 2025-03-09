package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.PLAYER_TABLE_JOIN;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class PlayerTableJoin extends Message
{

	/**
	 * @param serviceType
	 * @param msgType
	 * @param tableId
	 * @param txnMoney
	 */
	public PlayerTableJoin( long tableId, BigDecimal txnMoney )
	{
		super( 1, PLAYER_TABLE_JOIN, tableId );
		this.txnMoney = txnMoney;
	}

	private BigDecimal txnMoney;

	/**
	 * @return the txnMoney
	 */
	public BigDecimal getTxnMoney()
	{
		return txnMoney;
	}

}
