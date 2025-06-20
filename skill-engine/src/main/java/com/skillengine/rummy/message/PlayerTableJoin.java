package com.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.skillengine.rummy.message.MessageConstants.PLAYER_TABLE_JOIN;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerTableJoin extends Message {

    private String plrName;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param txnMoney
     */
    public PlayerTableJoin(long tableId, BigDecimal txnMoney, String plrName) {
        super(1, PLAYER_TABLE_JOIN, tableId);
        this.txnMoney = txnMoney;
        this.plrName = plrName;
    }

    private BigDecimal txnMoney;

    /**
     * @return the txnMoney
     */
    public BigDecimal getTxnMoney() {
        return txnMoney;
    }

    /**
     * @return the plrName
     */
    public String getPlrName() {
        return plrName;
    }

}
