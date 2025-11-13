package com.weather.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(env.getProperty("flyway.locations"))
                .baselineOnMigrate(Boolean.parseBoolean(env.getProperty("flyway.baselineOnMigrate")))
                .load();
    }
}
