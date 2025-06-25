package com.fcesur.cs.handler;

import com.fcesur.cs.message.HandShakeRequest;
import com.fcesur.cs.message.HandShakeResponse;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.ConnectionService;
import com.fcesur.cs.services.PlayerSession;

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
