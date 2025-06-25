package org.fcesur.gcs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Provides a DataSource instance based on config values in properties with the "member.database" prefix.
 */
@Configuration
@PropertySource({"classpath:application.properties"})
public class MemberDataSource {

    /**
     * Returns a DataSource instance based on config values in properties with the "member.database" prefix.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceMember() {
        return DataSourceBuilder.create().build();
    }
}