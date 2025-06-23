package com.connection.client.msg.handler;

import com.connection.client.msg.EchoRequest;
import com.connection.client.msg.EchoResponse;
import com.connection.jackson.JacksonObjectWrapper;
import com.connection.main.ConnectionServiceImpl;
import com.connection.msg.handler.MessageHandler;
import com.connection.services.PlayerSession;
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

        ConnectionServiceImpl.getInstance().getMessageDispatcher().sendMessage(session, response);
    }
}
