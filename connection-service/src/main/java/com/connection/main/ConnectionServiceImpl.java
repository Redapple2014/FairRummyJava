package com.connection.main;

import com.connection.jackson.JacksonObjectWrapper;
import com.connection.message.queue.CSMessageHandler;
import com.connection.message.queue.RabbitMQFrameworkImpl;
import com.connection.msg.MessageDigester;
import com.connection.msg.dispatcher.CS2ClientDispatcher;
import com.connection.msg.dispatcher.CS2ServiceDispatcher;
import com.connection.msg.dispatcher.MessageDispatcher;
import com.connection.netty.ServerInitializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ConnectionServiceImpl {
    private CS2ClientDispatcher clientDispatcher;
    private MessageDispatcher messageDispatcher;
    private JacksonObjectWrapper jacksonObjectWrapper;
    private ServerInitializer serverInitializer;
    private final int PORT = 4001;
    private static ConnectionServiceImpl instance = null;
    private MessageDigester messageDigester;
    private CS2ServiceDispatcher cs2ServiceDispatcher;
    private RabbitMQFrameworkImpl frameworkImpl;

    public static ConnectionServiceImpl init() {
        if ((instance == null)) {
            instance = new ConnectionServiceImpl();
        }
        return instance;

    }

    public ConnectionServiceImpl() {
        jacksonObjectWrapper = new JacksonObjectWrapper();
        clientDispatcher = new CS2ClientDispatcher();
        messageDispatcher = new MessageDispatcher(jacksonObjectWrapper);
        messageDigester = new MessageDigester(jacksonObjectWrapper);
        cs2ServiceDispatcher = new CS2ServiceDispatcher(jacksonObjectWrapper);
    }

    /**
     * @return the cs2ServiceDispatcher
     */
    public CS2ServiceDispatcher getCs2ServiceDispatcher() {
        return cs2ServiceDispatcher;
    }

    public CS2ClientDispatcher getClientDispatcher() {
        return clientDispatcher;
    }

    /**
     * @return the instance
     */
    public static ConnectionServiceImpl getInstance() {
        return instance;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void startTheWebServer() {
        try {
            serverInitializer = new ServerInitializer(PORT, jacksonObjectWrapper, messageDigester);
            serverInitializer.start();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @return the messageDigester
     */
    public MessageDigester getMessageDigester() {
        return messageDigester;
    }

    public void initMessageQueue() {

        if (log.isInfoEnabled()) {
            log.info("MQ ..");
        }

        final String queuePublisher = "ge";
        final String queueConsumer = "cs";

        try {

            frameworkImpl = new RabbitMQFrameworkImpl();

            frameworkImpl.registerQueuePublisher(queuePublisher);
            frameworkImpl.registerQueueConsumer(queueConsumer, new CSMessageHandler());

        } catch (Exception e) {
            log.error("Failed to init message queue", e);
        } finally {
            if (frameworkImpl != null) {
                try {
                    frameworkImpl.close();
                } catch (IOException e) {
                    log.error("Failed to close message queue", e);
                }
            }
        }
    }

    /**
     * @return the jacksonObjectWrapper
     */
    public JacksonObjectWrapper getJacksonObjectWrapper() {
        return jacksonObjectWrapper;
    }

    /**
     * @return the frameworkImpl
     */
    public RabbitMQFrameworkImpl getFrameworkImpl() {
        return frameworkImpl;
    }

}
