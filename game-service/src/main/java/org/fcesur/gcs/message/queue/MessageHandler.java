package org.fcesur.gcs.message.queue;

public interface MessageHandler {
    public void handleMessage(String message);
}
