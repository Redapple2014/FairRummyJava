package org.fcesur.cs.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import static org.fcesur.model.RabbitMQConfig.MQ_DEFAULT_AUTO_RECOVERY;
import static org.fcesur.model.RabbitMQConfig.MQ_DEFAULT_CONNECTION_TIMEOUT;
import static org.fcesur.model.RabbitMQConfig.MQ_DEFAULT_RECOVERY_INTERVAL;
import static org.fcesur.model.RabbitMQConfig.MQ_HOST;
import static org.fcesur.model.RabbitMQConfig.MQ_PASSWORD;
import static org.fcesur.model.RabbitMQConfig.MQ_PORT;
import static org.fcesur.model.RabbitMQConfig.MQ_USERNAME;
import static org.fcesur.model.RabbitMQConfig.MQ_VHOST;

@Slf4j
public final class RabbitMessageFramework implements MessageFramework {

    private final Connection connection;

    private final ConcurrentMap<String, Consumer> consumers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Publisher> publishers = new ConcurrentHashMap<>();

    /**
     * Constructor
     *
     * @throws TimeoutException Throws when a timeout occurs
     */
    public RabbitMessageFramework() throws TimeoutException, IOException {

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
