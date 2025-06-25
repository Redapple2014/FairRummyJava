package com.fcesur.cs;

import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.queue.CSMessageHandler;
import com.fcesur.cs.queue.RabbitMessageFramework;
import com.fcesur.cs.msg.MessageDigester;
import com.fcesur.cs.dispatcher.CS2ClientDispatcher;
import com.fcesur.cs.dispatcher.CS2ServiceDispatcher;
import com.fcesur.cs.dispatcher.MessageDispatcher;
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

    /**
     * Start the web server
     *
     * @throws InterruptedException
     */
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

            // log error
            log.error("Error connection to message queue", e);

            // close framework
            if (messageQueueFramework != null) {
                try {
                    messageQueueFramework.close();
                } catch (IOException e2) {
                    log.error("Failed to close message queue", e2);
                }
            }
        }
    }
}
