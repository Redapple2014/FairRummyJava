package org.fcesur.cs.msg;

import org.fcesur.cs.ConnectionService;
import org.fcesur.cs.handler.EchoHandler;
import org.fcesur.cs.handler.HandShakeHandler;
import org.fcesur.cs.handler.PingHandler;
import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.message.EchoRequest;
import org.fcesur.cs.message.HandShakeRequest;
import org.fcesur.cs.message.PingRequest;
import org.fcesur.cs.services.PlayerSession;
import org.fcesur.cs.services.ServiceMessage;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import static org.fcesur.messaging.ServiceType.CONN_SERVICE;
import static org.fcesur.messaging.ServiceType.GAME_SERVICE;

@Slf4j
public class MessageDigester {
    private JacksonObjectWrapper jacksonObjectWrapper;

    public MessageDigester(JacksonObjectWrapper jacksonObjectWrapper) {
        super();
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    public void messageProcessor(PlayerSession playerSession, Frame frames, MessageParser messageParser) {
        if (frames.getServiceType().equals(CONN_SERVICE)) {
            processCSMessage(playerSession, frames, messageParser);
        } else if (frames.getServiceType().equals(GAME_SERVICE)) {
            processGameEngineMessage(playerSession, messageParser, frames);
        }
    }

    private void processCSMessage(PlayerSession playerSession, Frame frames, MessageParser messageParser) {
        try {
            String msgType = jacksonObjectWrapper.getMsgType(messageParser.getSrvMsg());
            switch (msgType) {
                case MessageConstants.HAND_SHAKE:
                    HandShakeHandler handler = new HandShakeHandler(jacksonObjectWrapper);
                    handler.handleMessage(playerSession, jacksonObjectWrapper.readValue(messageParser.getSrvMsg(), HandShakeRequest.class));
                    break;
                case MessageConstants.PING_REQUEST:
                    PingHandler pingHandler = new PingHandler(jacksonObjectWrapper);
                    pingHandler.handleMessage(playerSession, jacksonObjectWrapper.readValue(messageParser.getSrvMsg(), PingRequest.class));
                    break;
                case MessageConstants.ECHO_REQUEST:
                    new EchoHandler(jacksonObjectWrapper).handleMessage(playerSession, jacksonObjectWrapper.readValue(messageParser.getSrvMsg(), EchoRequest.class));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Process game engine message
     *
     * @param session Player session
     * @param parser  Message parser
     * @param frame   Frame
     */
    private void processGameEngineMessage(@NonNull PlayerSession session,
                                          @NonNull MessageParser parser,
                                          @NonNull Frame frame) {

        if (log.isDebugEnabled()) {
            log.debug("GE Message: {}", parser.getSrvMsg());
        }

        try {

            String userSession = jacksonObjectWrapper.writeValueAsString(session);
            ServiceMessage message = new ServiceMessage(parser.getSrvMsg(), userSession, frame.getReceiverId());

            ConnectionService.getInstance()
                  .getMessageQueueFramework()
                  .publishToQueue(GAME_SERVICE, jacksonObjectWrapper.writeValueAsString(message));

        } catch (Exception e) {
            log.error("Failed to process GE message", e);
        }
    }
}
