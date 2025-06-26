package org.fcesur.skillengine.message.queue;

import java.io.IOException;

/**
 * Message framework
 */
public interface MessageFramework extends AutoCloseable {

    void registerQueuePublisher(String queueName) throws IOException;

    void registerQueueConsumer(String queueName, MessageHandler messageHandler) throws IOException;

    boolean publishToQueue(String queueName, String message);
}
