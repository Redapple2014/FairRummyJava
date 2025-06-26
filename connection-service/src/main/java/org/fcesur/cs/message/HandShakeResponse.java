package org.fcesur.cs.message;

import org.fcesur.cs.msg.MessageConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HandShakeResponse extends Message {

    private String dummyText;

    public HandShakeResponse(long userId, String dummyText) {
        super(MessageConstants.HAND_SHAKE_RESPONSE, userId);
        this.dummyText = dummyText;
    }

}
