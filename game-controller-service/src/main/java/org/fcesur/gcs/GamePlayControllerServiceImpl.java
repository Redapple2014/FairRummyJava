package org.fcesur.gcs;

import org.fcesur.gcs.message.queue.GCSMessageHandler;
import org.fcesur.gcs.message.queue.RabbitMQMessageFramework;

public class GamePlayControllerServiceImpl {
    private static GamePlayControllerServiceImpl instance = null;

    private RabbitMQMessageFramework frameworkImpl;

    public static GamePlayControllerServiceImpl init() {
        if (instance == null) {
            instance = new GamePlayControllerServiceImpl();
        }
        return instance;
    }

    public GamePlayControllerServiceImpl() {

    }

    public void initMessageQueue() {
        try {
            String queueConsumer = "gcs";
            frameworkImpl = new RabbitMQMessageFramework("");
            frameworkImpl.registerQueueConsumer(queueConsumer, new GCSMessageHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RabbitMQMessageFramework getFrameworkImpl() {
        return frameworkImpl;
    }
}
