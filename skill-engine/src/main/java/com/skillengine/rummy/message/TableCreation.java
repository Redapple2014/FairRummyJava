package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableCreation extends Message {
    private BigDecimal txnMoney;

    public TableCreation(long tableId) {
        super(1, MessageConstants.TABLE_CREATE, tableId);
    }

    /**
     * @return the txnMoney
     */
    public BigDecimal getTxnMoney() {
        return txnMoney;
    }

    /**
     * @param txnMoney the txnMoney to set
     */
    public void setTxnMoney(BigDecimal txnMoney) {
        this.txnMoney = txnMoney;
    }

}
