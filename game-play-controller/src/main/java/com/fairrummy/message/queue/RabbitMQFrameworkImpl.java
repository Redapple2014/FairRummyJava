package com.fairrummy.message.queue;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQFrameworkImpl implements MessageFramework {
    private Connection connection;

    private ConcurrentMap<String, Publisher> publishers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Consumer> listeners = new ConcurrentHashMap<>();

    public RabbitMQFrameworkImpl(String mqAlias) throws TimeoutException {
        String mqIP = "localhost";
        int mqPort = 5672;
        String mqUserName = "tester";
        String mqPassword = "tester";
        final boolean DEFAULT_AUTO_RECOVERY = true;
        final int DEFAULT_RECOVERY_INTERVAL = 1000;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(mqIP);
        connectionFactory.setPort(mqPort);
        connectionFactory.setUsername(mqUserName);
        connectionFactory.setPassword(mqPassword);
        connectionFactory.setVirtualHost("qa1");
        connectionFactory.setAutomaticRecoveryEnabled(DEFAULT_AUTO_RECOVERY);
        connectionFactory.setNetworkRecoveryInterval(DEFAULT_RECOVERY_INTERVAL);
        try {
            this.connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("this.connection.isOpen()" + this.connection.isOpen());
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

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
        Consumer consumer = listeners.putIfAbsent(queueName, tempConsumer);
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

}
