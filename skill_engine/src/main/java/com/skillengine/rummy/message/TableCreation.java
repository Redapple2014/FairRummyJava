package com.skillengine.rummy.message;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class TableCreation extends Message
{
	private BigDecimal txnMoney;

	public TableCreation( long tableId )
	{
		super( 1, MessageConstants.TABLE_CREATE, tableId );
	}

	/**
	 * @return the txnMoney
	 */
	public BigDecimal getTxnMoney()
	{
		return txnMoney;
	}

	/**
	 * @param txnMoney
	 *                the txnMoney to set
	 */
	public void setTxnMoney( BigDecimal txnMoney )
	{
		this.txnMoney = txnMoney;
	}

}
