package org.fcesur.skillengine.rummy.message;

public class ReshuffleCards extends Message {

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     */
    public ReshuffleCards(long tableId) {
        super(1, MessageConstants.RESHUFFLE, tableId);
    }

}
