package org.fcesur.messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import static org.fcesur.messaging.RabbitMQConfig.MQ_DEFAULT_AUTO_RECOVERY;
import static org.fcesur.messaging.RabbitMQConfig.MQ_DEFAULT_CONNECTION_TIMEOUT;
import static org.fcesur.messaging.RabbitMQConfig.MQ_DEFAULT_RECOVERY_INTERVAL;
import static org.fcesur.messaging.RabbitMQConfig.MQ_HOST;
import static org.fcesur.messaging.RabbitMQConfig.MQ_PASSWORD;
import static org.fcesur.messaging.RabbitMQConfig.MQ_PORT;
import static org.fcesur.messaging.RabbitMQConfig.MQ_USERNAME;
import static org.fcesur.messaging.RabbitMQConfig.MQ_VHOST;

public class RabbitMQMessageFramework implements MessageFramework {

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

        connectionFactory.setHost(MQ_HOST);
        connectionFactory.setPort(MQ_PORT);
        connectionFactory.setUsername(MQ_USERNAME);
        connectionFactory.setPassword(MQ_PASSWORD);
        connectionFactory.setVirtualHost(MQ_VHOST);
        connectionFactory.setAutomaticRecoveryEnabled(MQ_DEFAULT_AUTO_RECOVERY);
        connectionFactory.setNetworkRecoveryInterval(MQ_DEFAULT_RECOVERY_INTERVAL);
        connectionFactory.setConnectionTimeout(MQ_DEFAULT_CONNECTION_TIMEOUT);

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