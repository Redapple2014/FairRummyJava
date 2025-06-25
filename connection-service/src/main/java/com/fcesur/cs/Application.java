package com.fcesur.cs;

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

        final ConnectionService connectionServiceImpl =
              ConnectionService.init();

        // initialize queue
        connectionServiceImpl.initMessageQueue();

        // start web server
        connectionServiceImpl.startTheWebServer();
    }
}
