package org.fcesur.cs.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class QueuePublisher implements Publisher {

    private Connection connection;

    private Channel channel;

    private String queueName;

    public QueuePublisher(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
        try {
            this.channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean publishMessage(String message) {
        try {
            channel.basicPublish("", queueName, null, message.getBytes());
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
