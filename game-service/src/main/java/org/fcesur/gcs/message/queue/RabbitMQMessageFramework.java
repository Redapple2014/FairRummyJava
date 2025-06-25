package org.fcesur.gcs.message.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

public class RabbitMQMessageFramework implements MessageFramework {

    private static final String MQ_IP = "18.191.105.81";
    private static final Integer MQ_PORT = 5672;
    private static final String MQ_USER_NAME = "admin";
    private static final String MQ_PASSWORD = "admin@123";

    private static final boolean DEFAULT_AUTO_RECOVERY = true;
    private static final int DEFAULT_RECOVERY_INTERVAL = 1000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;

    private final Connection connection;

    private final ConcurrentMap<String, Publisher> publishers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Consumer> consumers = new ConcurrentHashMap<>();

    /**
     * Constructor
     *
     * @param mqAlias
     * @throws TimeoutException
     */
    public RabbitMQMessageFramework(String mqAlias) throws TimeoutException, IOException {

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
    public boolean registerQueuePublisher(String queueName) {
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
        }
        return false;
    }

    @Override
    public boolean registerQueueConsumer(String queueName, MessageHandler messageHandler) {
        boolean result = false;
        Consumer tempConsumer = new QueueConsumer(connection, queueName, messageHandler);
        Consumer consumer = consumers.putIfAbsent(queueName, tempConsumer);
        System.out.println("consumer" + consumer);
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
