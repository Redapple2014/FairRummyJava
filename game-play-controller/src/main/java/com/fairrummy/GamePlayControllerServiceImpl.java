package com.fairrummy;

import com.fairrummy.message.queue.GCSMessageHandler;
import com.fairrummy.message.queue.RabbitMQFrameworkImpl;

public class GamePlayControllerServiceImpl {
    private static GamePlayControllerServiceImpl instance = null;

    private RabbitMQFrameworkImpl frameworkImpl;

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
            frameworkImpl = new RabbitMQFrameworkImpl("");
            frameworkImpl.registerQueueConsumer(queueConsumer, new GCSMessageHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RabbitMQFrameworkImpl getFrameworkImpl() {
        return frameworkImpl;
    }
}
