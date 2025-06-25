package com.skillengine.message.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

public class RabbitMQMessageFramework implements MessageFramework {

    /* ========== Constant(s) ========== */

    private static final String MQ_IP = "18.191.105.81";
    private static final Integer MQ_PORT = 5672;
    private static final String MQ_USER_NAME = "admin";
    private static final String MQ_PASSWORD = "admin@123";
    private static final String MQ_VHOST = "/";

    private static final boolean DEFAULT_AUTO_RECOVERY = true;
    private static final int DEFAULT_RECOVERY_INTERVAL = 1_000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30_000;

    /* ========== Private member(s) ========== */

    private final Connection connection;

    private final ConcurrentMap<String, MessageQueuePublisher> publishers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, MessageQueueConsumer> consumers = new ConcurrentHashMap<>();

    /**
     * Constructor
     *
     * @throws TimeoutException Throws when timeout error occurs
     * @throws IOException      Throws when IO error occurs
     */
    public RabbitMQMessageFramework() throws TimeoutException, IOException {

        final ConnectionFactory connectionFactory =
              new ConnectionFactory();

        connectionFactory.setHost(MQ_IP);
        connectionFactory.setPort(MQ_PORT);
        connectionFactory.setUsername(MQ_USER_NAME);
        connectionFactory.setPassword(MQ_PASSWORD);
        connectionFactory.setVirtualHost(MQ_VHOST);
        connectionFactory.setAutomaticRecoveryEnabled(DEFAULT_AUTO_RECOVERY);
        connectionFactory.setNetworkRecoveryInterval(DEFAULT_RECOVERY_INTERVAL);
        connectionFactory.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);

        this.connection = connectionFactory.newConnection();
    }

    /**
     * Register queue publisher
     *
     * @param name Queue name
     * @throws IOException Throws when IO error occurs
     */
    @Override
    public void registerQueuePublisher(@NonNull String name) throws IOException {
        if (!publishers.containsKey(name)) {
            publishers.put(name, new RabbitMessageQueuePublisher(name, connection));
        }
    }

    @Override
    public void registerQueueConsumer(@NonNull String name, @NonNull MessageHandler messageHandler) throws IOException {
        if (!consumers.containsKey(name)) {
            consumers.put(name, new RabbitMQMessageQueueConsumer(name, messageHandler, connection));
        }
    }

    @Override
    public boolean publishToQueue(String queueName, String message) {
        MessageQueuePublisher publisher = publishers.get(queueName);
        return (publisher != null) && publisher.publishMessage(message);
    }

    @Override
    public void close() throws IOException {
        this.connection.close();
    }
}
