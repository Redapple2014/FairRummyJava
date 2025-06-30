package org.fcesur.cs.msg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Frame {

    @JsonProperty("authorization_token")
    @JsonAlias("authorizationtoken")
    private String authorizationtoken;

    @JsonProperty("message_type")
    @JsonAlias("messageType")
    private String messageType;

    @JsonProperty("service_type")
    @JsonAlias("serviceType")
    private String serviceType;

    @JsonProperty("message")
    @JsonAlias("message")
    private String message;

    @JsonProperty("timestamp")
    @JsonAlias("timestamp")
    private Long timestamp;

    @JsonProperty("receiver_id")
    @JsonAlias("receiverId")
    private Long receiverId;
}