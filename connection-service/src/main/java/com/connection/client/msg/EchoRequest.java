package com.connection.client.msg;

import com.connection.msg.MessageConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class EchoRequest extends Message {

    @JsonProperty("message")
    private String message;

    public EchoRequest() {
        super(MessageConstants.ECHO_REQUEST, 0L);
    }
}
