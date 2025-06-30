package org.fcesur.cs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fcesur.cs.dispatcher.CS2ClientDispatcher;
import org.fcesur.cs.dispatcher.CS2ServiceDispatcher;
import org.fcesur.cs.dispatcher.MessageDispatcher;
import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.msg.MessageDigester;
import org.fcesur.cs.netty.ServerInitializer;
import org.fcesur.cs.queue.CSMessageHandler;
import org.fcesur.messaging.RabbitMQMessageFramework;

import java.io.IOException;

import static org.fcesur.messaging.ServiceType.CONN_SERVICE;
import static org.fcesur.messaging.ServiceType.GAME_SERVICE;

@Slf4j
@Getter
public final class ConnectionService {

    private static final int PORT = 4001;

    /**
     * Singleton instance
     */
    private static ConnectionService INSTANCE = null;

    /* ========== Private member(s) ========== */

    private final JacksonObjectWrapper jacksonObjectWrapper;

    private final CS2ClientDispatcher clientDispatcher;
    private final CS2ServiceDispatcher serviceDispatcher;

    private final MessageDigester messageDigester;
    private final MessageDispatcher messageDispatcher;

    private ServerInitializer serverInitializer;
    private RabbitMQMessageFramework messageQueueFramework;

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

        serverInitializer = new ServerInitializer(PORT, messageDigester, jacksonObjectWrapper);

        serverInitializer.start();
    }

    /**
     * Initialize message queue
     */
    public void initMessageQueue() {

        try {

            messageQueueFramework = new RabbitMQMessageFramework("");

            messageQueueFramework.registerQueuePublisher(GAME_SERVICE);
            messageQueueFramework.registerQueueConsumer(CONN_SERVICE, new CSMessageHandler());

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
