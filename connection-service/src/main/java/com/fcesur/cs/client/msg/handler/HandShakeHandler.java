package com.fcesur.cs.client.msg.handler;

import com.fcesur.cs.client.msg.HandShakeRequest;
import com.fcesur.cs.client.msg.HandShakeResponse;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.main.ConnectionService;
import com.fcesur.cs.msg.handler.MessageHandler;
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
