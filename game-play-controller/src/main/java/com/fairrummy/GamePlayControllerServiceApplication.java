package com.fairrummy;

import com.fairrummy.utility.TableInfoCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@ComponentScan("com.fairrummy")
//@EnableRabbit
@EnableJpaRepositories(basePackages = {"com.fairrummy.mapper"})
public class GamePlayControllerServiceApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(GamePlayControllerServiceApplication.class, args);
        TableInfoCache.initializeTableInfoCache("", "");
        GamePlayControllerServiceImpl impl = new GamePlayControllerServiceImpl();
        impl.initMessageQueue();
    }
}
