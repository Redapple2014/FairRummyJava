package com.fcesur.cs.message.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class QueueConsumer implements Consumer {

    private Connection connection;
    private Channel channel;
    protected CustomMQConsumer consumer;
    private ConsumerListernerThread listernerThread;

    public QueueConsumer(Connection connection, String queueName, MessageHandler handler) {
        try {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            this.consumer = new CustomMQConsumer(channel);
            _makeConsumerRegistration(queueName);
            listernerThread = new ConsumerListernerThread(consumer, queueName, handler);
            listernerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void _makeConsumerRegistration(String queueOrTopicName) throws IOException {
        channel.basicConsume(queueOrTopicName, false, consumer);
    }

    @Override
    public void close() throws IOException {
        listernerThread.stopListening();
        listernerThread.stopWorkers();
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
