package org.fcesur.gcs;

import org.fcesur.gcs.utility.TableInfoCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableJpaRepositories(basePackages = {"org.fcesur.gcs.mapper"})
public class Application {

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        TableInfoCache.initializeTableInfoCache("", "");

        GamePlayControllerServiceImpl impl = new GamePlayControllerServiceImpl();
        impl.initMessageQueue();
    }
}
