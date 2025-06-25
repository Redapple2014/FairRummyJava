package com.skillengine.main;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Skill engine
 */
@Slf4j
public final class Application {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        final SkillEngine engine = SkillEngine.init();

        try {

            // initialize message queue
            engine.initMessageQueue();

        } catch (IOException | TimeoutException e) {

            // log error
            log.error("Error connection to message queue", e);

            // close framework
            try {
                engine.close();
            } catch (Exception e2) {
                log.error("Failed to close skill engine", e2);
            }

            // exit
            System.exit(1);
        }
    }
}