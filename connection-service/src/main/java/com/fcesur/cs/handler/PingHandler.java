package com.fcesur.cs.handler;

import com.fcesur.cs.message.PingRequest;
import com.fcesur.cs.message.PingResponse;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.ConnectionService;
import com.fcesur.cs.services.PlayerSession;

public class PingHandler implements MessageHandler<PingRequest> {

    private JacksonObjectWrapper jacksonObjectWrapper;

    public PingHandler(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    @Override
    public void handleMessage(PlayerSession session, PingRequest message) {

        PingResponse pingResponse = new PingResponse(message.getUserId(), System.currentTimeMillis());
        String response = jacksonObjectWrapper.writeValueAsString(pingResponse);
        ConnectionService.getInstance().getMessageDispatcher().sendMessage(session, response);
    }
}
