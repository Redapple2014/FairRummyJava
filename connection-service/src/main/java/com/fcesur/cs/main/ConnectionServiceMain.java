package com.fcesur.cs.main;

import lombok.extern.slf4j.Slf4j;

/**
 * Connection service
 */
@Slf4j
public final class ConnectionServiceMain {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        final ConnectionService connectionServiceImpl =
              ConnectionService.init();

        connectionServiceImpl.initMessageQueue();

        try {
            connectionServiceImpl.startTheWebServer();
        } catch (InterruptedException e) {
            log.error("Failed to start the server", e);
        }
    }
}
