package org.fcesur.cs;

import lombok.extern.slf4j.Slf4j;

/**
 * Connection service
 */
@Slf4j
public final class Application {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) throws InterruptedException {

        final ConnectionService connectionService = ConnectionService.init();

        // initialize queue
        connectionService.initMessageQueue();

        // start web server
        connectionService.startTheWebServer();
    }
}
