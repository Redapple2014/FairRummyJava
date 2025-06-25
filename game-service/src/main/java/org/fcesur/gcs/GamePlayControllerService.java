package org.fcesur.gcs;

import lombok.Getter;
import org.fcesur.gcs.message.queue.GCSMessageHandler;
import org.fcesur.gcs.message.queue.RabbitMQMessageFramework;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Getter
public final class GamePlayControllerService {

    private static final String QUEUE_NAME = "gcs";

    private static GamePlayControllerService instance = null;


    private RabbitMQMessageFramework framework;

    /**
     * Constructor
     */
    public GamePlayControllerService() {
    }

    public static GamePlayControllerService init() {
        if (instance == null) {
            instance = new GamePlayControllerService();
        }
        return instance;
    }

    public void initMessageQueue() throws IOException, TimeoutException {
        framework = new RabbitMQMessageFramework("");
        framework.registerQueueConsumer(QUEUE_NAME, new GCSMessageHandler());
    }
}
