package com.fcesur.cs.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Frame {

    private String authorizationtoken;
    private String serviceType;
    private Long timestamp;
    private Long receiverId;
}