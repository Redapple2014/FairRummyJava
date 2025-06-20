package com.connection.client.msg.handler;

import com.connection.client.msg.PingRequest;
import com.connection.client.msg.PingResponse;
import com.connection.jackson.JacksonObjectWrapper;
import com.connection.main.ConnectionServiceImpl;
import com.connection.msg.MessageConstants;
import com.connection.msg.handler.MessageHandler;
import com.connection.services.PlayerSession;

public class PingHandler implements MessageHandler<PingRequest> {

    private JacksonObjectWrapper jacksonObjectWrapper;

    public PingHandler(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    @Override
    public void handleMessage(PlayerSession session, PingRequest message) {

        PingResponse pingResponse = new PingResponse(message.getUserId(), System.currentTimeMillis());
        String response = jacksonObjectWrapper.writeValueAsString(pingResponse);
        ConnectionServiceImpl.getInstance().getMessageDispatcher().sendMessage(session, response);

    }

}
