package com.krimo.event.config;


import com.krimo.event.container.PostgresTestContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresContainerEnv {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}
