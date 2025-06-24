package com.fcesur.cs.main;

import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.message.queue.CSMessageHandler;
import com.fcesur.cs.message.queue.RabbitMessageFramework;
import com.fcesur.cs.msg.MessageDigester;
import com.fcesur.cs.msg.dispatcher.CS2ClientDispatcher;
import com.fcesur.cs.msg.dispatcher.CS2ServiceDispatcher;
import com.fcesur.cs.msg.dispatcher.MessageDispatcher;
import com.fcesur.cs.netty.ServerInitializer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Getter
public final class ConnectionService {

    private static final int PORT = 4001;

    private static ConnectionService INSTANCE = null;


    private final JacksonObjectWrapper jacksonObjectWrapper;

    private final CS2ClientDispatcher clientDispatcher;
    private final CS2ServiceDispatcher serviceDispatcher;

    private final MessageDigester messageDigester;
    private final MessageDispatcher messageDispatcher;

    private ServerInitializer serverInitializer;
    private RabbitMessageFramework messageQueueFramework;

    /**
     * Constructor
     */
    private ConnectionService() {
        this.jacksonObjectWrapper = new JacksonObjectWrapper();
        this.clientDispatcher = new CS2ClientDispatcher();
        this.serviceDispatcher = new CS2ServiceDispatcher(jacksonObjectWrapper);
        this.messageDispatcher = new MessageDispatcher(jacksonObjectWrapper);
        this.messageDigester = new MessageDigester(jacksonObjectWrapper);
    }

    /**
     * Initialize connection service
     *
     * @return Connection service
     */
    public static ConnectionService init() {
        if ((INSTANCE == null)) {
            INSTANCE = new ConnectionService();
        }
        return INSTANCE;

    }

    /**
     * Get instance of connection service
     *
     * @return Connection service
     */
    public static ConnectionService getInstance() {
        return INSTANCE;
    }

    public void startTheWebServer() throws InterruptedException {

        serverInitializer = new ServerInitializer(PORT, jacksonObjectWrapper, messageDigester);

        serverInitializer.start();
    }

    /**
     * Initialize message queue
     */
    public void initMessageQueue() {

        final String queuePublisher = "ge";
        final String queueConsumer = "cs";

        try {

            messageQueueFramework = new RabbitMessageFramework();

            messageQueueFramework.registerQueuePublisher(queuePublisher);
            messageQueueFramework.registerQueueConsumer(queueConsumer, new CSMessageHandler());

            log.info("Message queue initialized");

        } catch (Exception e) {
            log.error("Error connection to message queue", e);
        } finally {
            if (messageQueueFramework != null) {
                try {
                    messageQueueFramework.close();
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
    public RabbitMessageFramework getMessageQueueFramework() {
        return messageQueueFramework;
    }

}
