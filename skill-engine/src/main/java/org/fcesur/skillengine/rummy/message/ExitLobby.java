package org.fcesur.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExitLobby extends Message {

    private long userId;
    private String reason;

    public ExitLobby(long tableId, long userId, String reason) {
        super(1, "exitlobby", tableId);
        this.userId = userId;
        this.reason = reason;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    public long getUserId() {
        return userId;
    }

}
