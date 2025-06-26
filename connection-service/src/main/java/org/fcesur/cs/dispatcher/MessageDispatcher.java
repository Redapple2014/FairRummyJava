package org.fcesur.cs.dispatcher;

import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.ConnectionService;
import org.fcesur.cs.msg.SessionDetails;
import org.fcesur.cs.msg.TransmittedMessage;
import org.fcesur.cs.services.PlayerSession;

public class MessageDispatcher {

    private JacksonObjectWrapper jacksonObjectWrapper = null;

    public MessageDispatcher(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    public void sendMessage(PlayerSession playerSession, String payload) {
        SessionDetails details = new SessionDetails(playerSession.getUserID(), serialize(playerSession, payload));
        ConnectionService.getInstance().getClientDispatcher().enQueue(details);
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
