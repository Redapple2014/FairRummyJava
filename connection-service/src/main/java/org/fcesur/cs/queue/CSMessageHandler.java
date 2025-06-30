package org.fcesur.cs.queue;

import org.fcesur.cs.ConnectionService;
import org.fcesur.cs.services.PlayerSession;
import org.fcesur.cs.services.ServiceMessage;
import org.fcesur.messaging.MessageHandler;

public class CSMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        ServiceMessage serviceMessage = ConnectionService.getInstance().getJacksonObjectWrapper().readValue(message, ServiceMessage.class);
        PlayerSession playerSession = ConnectionService.getInstance().getJacksonObjectWrapper().readValue(serviceMessage.getPlayerSession(), PlayerSession.class);
        ConnectionService.getInstance().getMessageDispatcher().sendMessage(playerSession, serviceMessage.getGamePayload());

    }

}
