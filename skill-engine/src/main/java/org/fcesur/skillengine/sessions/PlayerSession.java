package org.fcesur.skillengine.sessions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerSession {

    private long channelID;
    private long userID;
    private int serviceId;

    /**
     * @return the serviceId
     */
    public int getServiceId() {
        return serviceId;
    }

    public PlayerSession(long channelID, long userId, int serviceId) {
        this.channelID = channelID;
        this.userID = userId;
        this.serviceId = serviceId;
    }

    public long getUserID() {
        return userID;
    }

    public long getChannelID() {
        return channelID;
    }

    @Override
    public String toString() {
        return "PlayerSession [channelID=" + channelID + ", userID=" + userID + "]";
    }

}
