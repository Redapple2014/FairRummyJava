package com.fcesur.cs.handler;

import com.fcesur.cs.message.EchoRequest;
import com.fcesur.cs.message.EchoResponse;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.ConnectionService;
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
