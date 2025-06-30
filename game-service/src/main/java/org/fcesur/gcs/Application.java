package org.fcesur.gcs;

import org.fcesur.messaging.model.TableInfoCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@ComponentScan(basePackages = "org.fcesur.gcs")
@EnableJpaRepositories(basePackages = "org.fcesur.gcs.mapper")
public class Application {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        TableInfoCache.initializeTableInfoCache("", "");

        GamePlayControllerService impl = new GamePlayControllerService();

        try {
            impl.initMessageQueue();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
