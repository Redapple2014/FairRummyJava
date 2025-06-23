package com.skillengine.rummy.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Game history message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class PingMessage extends Message {

    /**
     * Constructor
     */
    public PingMessage() {
        super(1, MessageConstants.PING, 0L);
    }
}