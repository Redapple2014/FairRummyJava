package org.fcesur.skillengine.message.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Queue publisher
 */
@Slf4j
public final class RabbitMessageQueuePublisher implements MessageQueuePublisher {

    private static final String EXCHANGE = "";

    private final String name;
    private final Channel channel;

    /**
     * Constructor
     *
     * @param name       Queue name
     * @param connection Queue Connection
     * @throws IOException Throws if any I/O error occurs
     */
    public RabbitMessageQueuePublisher(@NonNull String name, @NonNull Connection connection) throws IOException {

        this.name = name;
        this.channel = connection.createChannel();

        channel.queueDeclare(name, true, false, false, null);
    }

    @Override
    public boolean publishMessage(@NonNull String message) {
        try {
            channel.basicPublish(EXCHANGE, name, null, message.getBytes(UTF_8));
            return true;
        } catch (IOException e) {
            log.error("Failed to publish message", e);
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            channel.close();
        } catch (Exception e) {
            log.error("Failed to close message queue channel", e);
        }
    }
}