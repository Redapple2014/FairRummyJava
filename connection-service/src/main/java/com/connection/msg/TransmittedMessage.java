package com.connection.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransmittedMessage {
    private long userId;
    private long timestamp;
    private long receiverId;
    private String payload;
}
