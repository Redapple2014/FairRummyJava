package com.fcesur.cs.message.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

@Slf4j
public final class RabbitMessageFramework implements MessageFramework {

    private static final String MQ_IP = "18.191.105.81";
    private static final int MQ_PORT = 5672;
    private static final String MQ_USER_NAME = "admin";
    private static final String MQ_PASSWORD = "admin@123";

    private static final boolean DEFAULT_AUTO_RECOVERY = true;
    private static final int DEFAULT_RECOVERY_INTERVAL = 1_000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10_000;

    private Connection connection;

    private final ConcurrentMap<String, Consumer> consumers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Publisher> publishers = new ConcurrentHashMap<>();

    /**
     * Constructor
     *
     * @throws TimeoutException Throws when a timeout occurs
     */
    public RabbitMessageFramework() throws TimeoutException, IOException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(MQ_IP);
        connectionFactory.setPort(MQ_PORT);
        connectionFactory.setUsername(MQ_USER_NAME);
        connectionFactory.setPassword(MQ_PASSWORD);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(DEFAULT_AUTO_RECOVERY);
        connectionFactory.setNetworkRecoveryInterval(DEFAULT_RECOVERY_INTERVAL);
        connectionFactory.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);

        this.connection = connectionFactory.newConnection();
    }

    @Override
    public boolean registerQueuePublisher(@NonNull String queueName) {
        if (!publishers.containsKey(queueName)) {
            publishers.put(queueName, new QueuePublisher(connection, queueName));
        }
        return false;
    }

    @Override
    public boolean publishToQueue(String queueName, String message) {
        boolean result = false;
        Publisher messagePublisher = publishers.get(queueName);
        if (messagePublisher != null) {
            result = messagePublisher.publishMessage(message);
            System.out.println("Inside " + message + "result" + result);
        }
        return false;
    }

    @Override
    public boolean registerQueueConsumer(String queueName, MessageHandler messageHandler) {
        boolean result = false;
        Consumer tempConsumer = new QueueConsumer(connection, queueName, messageHandler);
        Consumer consumer = consumers.putIfAbsent(queueName, tempConsumer);
        if (consumer != null) {
            try {
                tempConsumer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result = true;
        }
        return result;

    }

    @Override
    public void close() throws IOException {
        this.connection.close();
    }
}
