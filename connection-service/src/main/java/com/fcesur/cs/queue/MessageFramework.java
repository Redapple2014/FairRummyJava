package com.fcesur.cs.queue;

import org.jspecify.annotations.NonNull;

import java.io.Closeable;

/**
 * Message framework
 */
public interface MessageFramework extends Closeable {

    boolean registerQueuePublisher(@NonNull String queueName);

    boolean publishToQueue(@NonNull String queueName, @NonNull String message);

    boolean registerQueueConsumer(@NonNull String queueName, @NonNull MessageHandler messageHandler);
}
