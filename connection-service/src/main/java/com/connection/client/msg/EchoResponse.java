package com.connection.client.msg;

import com.connection.msg.MessageConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EchoResponse extends Message {

    @JsonProperty("message")
    private String message;

    public EchoResponse(String message) {
        super(MessageConstants.ECHO_RESPONSE, 0L);

        this.message = message;
    }
}
