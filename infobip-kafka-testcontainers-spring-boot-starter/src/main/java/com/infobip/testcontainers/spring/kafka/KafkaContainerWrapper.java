package com.infobip.testcontainers.spring.kafka;

import org.testcontainers.containers.KafkaContainer;

public class KafkaContainerWrapper extends KafkaContainer {

    public KafkaContainerWrapper() {
    }

    public KafkaContainerWrapper(String confluentPlatformVersion) {
        super(confluentPlatformVersion);
    }
}
