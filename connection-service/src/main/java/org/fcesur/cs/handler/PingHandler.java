package org.fcesur.cs.handler;

import org.fcesur.cs.message.PingRequest;
import org.fcesur.cs.message.PingResponse;
import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.ConnectionService;
import org.fcesur.cs.services.PlayerSession;

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
