package com.connection.main;

/**
 * Connection service
 */
public final class ConnectionServiceMain {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        ConnectionServiceImpl connectionServiceImpl = ConnectionServiceImpl.init();

        connectionServiceImpl.initMessageQueue();
        connectionServiceImpl.startTheWebServer();
    }
}
