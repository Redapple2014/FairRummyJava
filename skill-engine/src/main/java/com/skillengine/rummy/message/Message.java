package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Message
 */
@Getter
@NoArgsConstructor
@ToString
public abstract class Message {

    private int serviceType;
    private String messageType;
    private long tableId;

    /**
     * Constructor
     *
     * @param serviceType Service type
     * @param msgType     Message type
     * @param tableId     Table id
     */
    protected Message(int serviceType, String msgType, long tableId) {
        this.serviceType = serviceType;
        this.messageType = msgType;
        this.tableId = tableId;
    }
}
