package com.krimo.ticket.config;

import com.krimo.ticket.container.MongoTestContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MongoContainerEnv {

    @Container
    public static GenericContainer mongoDBContainer = MongoTestContainer.getInstance();


}
