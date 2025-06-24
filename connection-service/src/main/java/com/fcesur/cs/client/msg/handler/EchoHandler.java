package com.fcesur.cs.client.msg.handler;

import com.fcesur.cs.client.msg.EchoRequest;
import com.fcesur.cs.client.msg.EchoResponse;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.main.ConnectionService;
import com.fcesur.cs.msg.handler.MessageHandler;
import com.fcesur.cs.services.PlayerSession;
import org.jspecify.annotations.NonNull;

/**
 * Echo handler
 */
public final class EchoHandler implements MessageHandler<EchoRequest> {

    private final JacksonObjectWrapper jacksonObjectWrapper;

    /**
     * Constructor
     *
     * @param jacksonObjectWrapper Jackson object wrapper
     */
    public EchoHandler(JacksonObjectWrapper jacksonObjectWrapper) {
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    @Override
    public void handleMessage(@NonNull PlayerSession session, @NonNull EchoRequest request) {

        PlayerSession playerSession = new PlayerSession(0L, 0L, 12345);
        String response = jacksonObjectWrapper.writeValueAsString(new EchoResponse(request.getMessage()));

        ConnectionService.getInstance().getMessageDispatcher().sendMessage(session, response);
    }
}
