package org.fcesur.cs.message;

import org.fcesur.cs.msg.MessageConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PingResponse extends Message {
    private long curTime;

    /**
     * @param msgType
     * @param userId
     * @param curTime
     */
    public PingResponse(long userId, long curTime) {
        super(MessageConstants.PING_REPONSE, userId);
        this.curTime = curTime;
    }

}
