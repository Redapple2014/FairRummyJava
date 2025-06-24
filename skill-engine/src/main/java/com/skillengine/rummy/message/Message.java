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
     * @param messageType Message type
     * @param tableId     Table id
     */
    protected Message(int serviceType, String messageType, long tableId) {
        this.serviceType = serviceType;
        this.messageType = messageType;
        this.tableId = tableId;
    }
}
