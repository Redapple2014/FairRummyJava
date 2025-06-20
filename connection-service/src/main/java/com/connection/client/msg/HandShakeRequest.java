package com.connection.client.msg;

import com.connection.msg.MessageConstants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HandShakeRequest extends Message {

    public HandShakeRequest(String msgType, long userId) {
        super(MessageConstants.HAND_SHAKE, userId);

    }

}
