package com.infobip.testcontainers.spring.rabbit;

import org.testcontainers.containers.GenericContainer;

public class RabbitContainerWrapper extends GenericContainer<RabbitContainerWrapper> {

    public static final int PORT = 5672;

    public RabbitContainerWrapper(String image) {
        super(image);
        withExposedPorts(PORT);
    }
}