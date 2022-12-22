package com.krimo.ticket.container;

import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;

public class MongoTestContainer extends GenericContainer<MongoTestContainer> {

    public static final int MONGODB_PORT = 27017;
    public static final String IMAGE_VERSION = "mongo:4.4.3";
    public static GenericContainer container;

    public MongoTestContainer () {
        super(IMAGE_VERSION);
        addExposedPort(MONGODB_PORT);
    }

    @NotNull
    public Integer getPort() {
        return getMappedPort(MONGODB_PORT);
    }


    public static GenericContainer getInstance() {
        if (container == null) {
            container = new MongoTestContainer();
        }

        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

}
