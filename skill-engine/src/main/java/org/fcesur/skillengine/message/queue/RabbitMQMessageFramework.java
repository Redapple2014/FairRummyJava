package org.fcesur.skillengine.message.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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

public class RabbitMQMessageFramework implements MessageFramework {

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
