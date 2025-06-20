package com.connection.msg.dispatcher;

import com.connection.jackson.JacksonObjectWrapper;
import com.connection.main.ConnectionServiceImpl;
import com.connection.msg.SessionDetails;
import com.connection.msg.TransmittedMessage;
import com.connection.services.PlayerSession;

public class MessageDispatcher {

    private JacksonObjectWrapper jacksonObjectWrapper = null;

    public MessageDispatcher(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    public void sendMessage(PlayerSession playerSession, String payload) {
        SessionDetails details = new SessionDetails(playerSession.getUserID(), serialize(playerSession, payload));
        ConnectionServiceImpl.getInstance().getClientDispatcher().enQueue(details);
    }

    private String serialize(PlayerSession playerSession, String payload) {
        TransmittedMessage transmittedMessage = new TransmittedMessage();
        transmittedMessage.setTimestamp(System.currentTimeMillis());
        transmittedMessage.setUserId(playerSession.getUserID());
        transmittedMessage.setReceiverId(-1l);
        try {
            transmittedMessage.setPayload(payload);
            return jacksonObjectWrapper.writeValueAsString(transmittedMessage);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
