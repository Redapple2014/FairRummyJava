package com.fcesur.cs.client.msg;

import com.fcesur.cs.msg.MessageConstants;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HandShakeRequest extends Message {

    public HandShakeRequest(String msgType, long userId) {
        super(MessageConstants.HAND_SHAKE, userId);

    }

}
