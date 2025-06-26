package org.fcesur.cs.handler;

import org.fcesur.cs.message.HandShakeRequest;
import org.fcesur.cs.message.HandShakeResponse;
import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.ConnectionService;
import org.fcesur.cs.services.PlayerSession;

public class HandShakeHandler implements MessageHandler<HandShakeRequest> {

    private JacksonObjectWrapper jacksonObjectWrapper;

    public HandShakeHandler(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    @Override
    public void handleMessage(PlayerSession session, HandShakeRequest message) {
        HandShakeResponse handShakeResponse = new HandShakeResponse(session.getUserID(), "Sending the userId " + message.getUserId());
        ConnectionService.getInstance().getMessageDispatcher().sendMessage(session, jacksonObjectWrapper.writeValueAsString(handShakeResponse));
    }

}
