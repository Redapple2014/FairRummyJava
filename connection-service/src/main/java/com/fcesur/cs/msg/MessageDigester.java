package com.fcesur.cs.msg;

import com.fcesur.cs.message.EchoRequest;
import com.fcesur.cs.message.HandShakeRequest;
import com.fcesur.cs.message.PingRequest;
import com.fcesur.cs.handler.EchoHandler;
import com.fcesur.cs.handler.HandShakeHandler;
import com.fcesur.cs.handler.PingHandler;
import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.ConnectionService;
import com.fcesur.cs.services.PlayerSession;
import com.fcesur.cs.services.ServiceMessage;

import static com.fcesur.cs.services.ServiceTypes.CONNECTION_SERVICE;
import static com.fcesur.cs.services.ServiceTypes.GAME_SERVICE;

public class MessageDigester {
    private JacksonObjectWrapper jacksonObjectWrapper;

    public MessageDigester(JacksonObjectWrapper jacksonObjectWrapper) {
        super();
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    public void messageProcessor(PlayerSession playerSession, Frame frames, MessageParser messageParser) {
        switch (frames.getServiceType()) {
            case CONNECTION_SERVICE:
                processCSMessage(playerSession, frames, messageParser);
                break;
            case GAME_SERVICE:
                procesGEMessage(playerSession, messageParser, frames);
                break;
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

    private void procesGEMessage(PlayerSession playerSession, MessageParser messageParser, Frame frames) {

        try {
            System.out.println("GE Message" + messageParser);
            String userSession = jacksonObjectWrapper.writeValueAsString(playerSession);
            ServiceMessage message = new ServiceMessage(messageParser.getSrvMsg(), userSession, frames.getReceiverId());
            ConnectionService.getInstance().getMessageQueueFramework().publishToQueue("ge", jacksonObjectWrapper.writeValueAsString(message));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
