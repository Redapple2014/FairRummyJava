package org.fcesur.skillengine.message.queue;

import java.io.Closeable;

/**
 * Message queue publisher
 */
public interface MessageQueuePublisher extends Closeable {

    /**
     * Publish message
     *
     * @param message Message
     * @return True if message was published, false otherwise
     */
    boolean publishMessage(String message);
}
