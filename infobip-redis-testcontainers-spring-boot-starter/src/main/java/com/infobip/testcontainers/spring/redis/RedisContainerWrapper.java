package com.infobip.testcontainers.spring.redis;

import org.testcontainers.containers.GenericContainer;

public class RedisContainerWrapper extends GenericContainer<RedisContainerWrapper> {

    public static final int PORT = 6379;

    public RedisContainerWrapper() {
        this("redis:6.2.6-alpine");
    }

    public RedisContainerWrapper(String dockerImageName) {
        super(dockerImageName);
        withExposedPorts(PORT);
    }
}
