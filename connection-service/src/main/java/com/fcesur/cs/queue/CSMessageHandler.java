package com.fcesur.cs.queue;

import com.fcesur.cs.ConnectionService;
import com.fcesur.cs.services.PlayerSession;
import com.fcesur.cs.services.ServiceMessage;

public class CSMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        ServiceMessage serviceMessage = ConnectionService.getInstance().getJacksonObjectWrapper().readValue(message, ServiceMessage.class);
        PlayerSession playerSession = ConnectionService.getInstance().getJacksonObjectWrapper().readValue(serviceMessage.getPlayerSession(), PlayerSession.class);
        ConnectionService.getInstance().getMessageDispatcher().sendMessage(playerSession, serviceMessage.getGamePayload());

    }

}
