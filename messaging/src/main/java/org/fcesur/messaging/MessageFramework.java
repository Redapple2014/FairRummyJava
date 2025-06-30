package org.fcesur.messaging;

import java.io.Closeable;

public interface MessageFramework extends Closeable {

    boolean registerQueuePublisher(String queueName);

    boolean publishToQueue(String queueName, String message);

    boolean registerQueueConsumer(String queueName, MessageHandler messageHandler);

}