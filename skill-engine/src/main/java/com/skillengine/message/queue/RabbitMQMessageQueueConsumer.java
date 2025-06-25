package com.skillengine.message.queue;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ message queue consumer implementation
 */
@Slf4j
public final class RabbitMQMessageQueueConsumer implements MessageQueueConsumer {

    private final Channel channel;
    private final ConsumerListernerThread listernerThread;

    protected CustomMQConsumer consumer;

    /**
     * Constructor
     *
     * @param name       Queue name
     * @param handler    Message handler
     * @param connection Connection
     */
    public RabbitMQMessageQueueConsumer(@NonNull String name, @NonNull MessageHandler handler, @NonNull Connection connection) throws IOException {

        log.info("Queue consumer initialized for queue {}", name);

        channel = connection.createChannel();

        DeclareOk declareOk = channel.queueDeclare(name, true, false, false, null);

        this.consumer = new CustomMQConsumer(channel);

        channel.basicConsume(name, false, consumer);

        listernerThread = new ConsumerListernerThread(consumer, name, handler);
        listernerThread.start();
    }

    @Override
    public void close() throws IOException {

        listernerThread.stopListening();
        listernerThread.stopWorkers();

        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            log.error("Failed to close message queue channel", e);
        }
    }
}
