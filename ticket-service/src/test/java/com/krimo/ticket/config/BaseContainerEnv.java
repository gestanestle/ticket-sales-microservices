package com.krimo.ticket.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

public class BaseContainerEnv {

    @ServiceConnection
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"));

    static DataSource dataSource() {
        PGSimpleDataSource dataSource  = new PGSimpleDataSource();
        dataSource.setURL(BaseContainerEnv.container.getJdbcUrl());
        dataSource.setUser(BaseContainerEnv.container.getUsername());
        dataSource.setPassword(BaseContainerEnv.container.getPassword());
        return dataSource;
    }

    @BeforeAll
    static void beforeAll() {
        if (!container.isRunning()) container.start();
        Flyway flyway = new Flyway(Flyway.configure().locations("/db/migration").dataSource(dataSource()));
        flyway.clean();
        flyway.migrate();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }
}
