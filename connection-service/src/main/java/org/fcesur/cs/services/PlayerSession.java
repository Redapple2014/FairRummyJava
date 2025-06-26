package org.fcesur.cs.services;

public class PlayerSession {

    private long channelID;
    private long userID;
    private int serviceId;

    public PlayerSession() {
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

    /**
     * @return the serviceId
     */
    public int getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return "PlayerSession [channelID=" + channelID + ", userID=" + userID + "]";
    }

}
