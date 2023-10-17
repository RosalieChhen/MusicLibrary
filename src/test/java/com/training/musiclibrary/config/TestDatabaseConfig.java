package com.training.musiclibrary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class TestDatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.h2-test")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
