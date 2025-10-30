package com.weather.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(
                        "jdbc:postgresql://localhost:5432/weatherdb",
                        "weatheruser",
                        "weatherpass123"
                )
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}
