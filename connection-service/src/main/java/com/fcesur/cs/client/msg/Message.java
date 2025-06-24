package com.fcesur.cs.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Message {
    private String msgType;
    private long userId;
}
