package com.krimo.event.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1" ;
    private static final String DB_NAME = "test";
    private static PostgreSQLContainer container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer().withDatabaseName(DB_NAME);
        }

        return container;
    }

    @Override
    public void start() {
        super.start();

        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
