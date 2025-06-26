package org.fcesur.model;

/**
 * RabbitMQ configuration
 */
public final class RabbitMQConfig {

    public static final String MQ_HOST = "18.191.105.81";
    public static final Integer MQ_PORT = 5672;
    public static final String MQ_USERNAME = "admin";
    public static final String MQ_PASSWORD = "admin@123";
    public static final String MQ_VHOST = "/";

    public static final boolean MQ_DEFAULT_AUTO_RECOVERY = true;
    public static final int MQ_DEFAULT_RECOVERY_INTERVAL = 1_000;
    public static final int MQ_DEFAULT_CONNECTION_TIMEOUT = 30_000;

    // public static final String MQ_EXCHANGE_CONN_SERVICE = "cs";
    // public static final String MQ_EXCHANGE_GAME_ENGINE = "ge";

    private RabbitMQConfig() {
    }
}
