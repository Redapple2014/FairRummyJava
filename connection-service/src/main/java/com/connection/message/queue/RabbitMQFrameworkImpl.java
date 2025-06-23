package com.connection.message.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

public class RabbitMQFrameworkImpl implements MessageFramework {

    private Connection connection;

    private ConcurrentMap<String, Publisher> publishers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Consumer> listeners = new ConcurrentHashMap<>();

    public RabbitMQFrameworkImpl() throws TimeoutException {

        String mqIP = "18.191.105.81";
        int mqPort = 5672;
        String mqUserName = "admin";
        String mqPassword = "admin@123";

        final boolean DEFAULT_AUTO_RECOVERY = true;
        final int DEFAULT_RECOVERY_INTERVAL = 1000;

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(mqIP);
        connectionFactory.setPort(mqPort);
        connectionFactory.setUsername(mqUserName);
        connectionFactory.setPassword(mqPassword);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(DEFAULT_AUTO_RECOVERY);
        connectionFactory.setNetworkRecoveryInterval(DEFAULT_RECOVERY_INTERVAL);
        connectionFactory.setConnectionTimeout(10_000);

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
            System.out.println("Inside " + message + "result" + result);
        }
        return false;
    }

    @Override
    public boolean registerQueueConsumer(String queueName, MessageHandler messageHandler) {
        boolean result = false;
        Consumer tempConsumer = new QueueConsumer(connection, queueName, messageHandler);
        Consumer consumer = listeners.putIfAbsent(queueName, tempConsumer);
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
