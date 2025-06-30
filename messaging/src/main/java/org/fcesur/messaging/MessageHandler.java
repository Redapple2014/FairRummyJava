package org.fcesur.messaging;

import org.jspecify.annotations.NonNull;

/**
 * Message handler
 */
public interface MessageHandler {

    /**
     * Handle message
     *
     * @param message message
     */
    void handleMessage(@NonNull String message);
}
