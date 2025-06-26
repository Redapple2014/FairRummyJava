package org.fcesur.skillengine.message.queue;

public interface MessageHandler {
    public void handleMessage(String message);
}
