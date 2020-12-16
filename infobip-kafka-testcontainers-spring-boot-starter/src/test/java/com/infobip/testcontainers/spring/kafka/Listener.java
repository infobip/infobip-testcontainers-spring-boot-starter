package com.infobip.testcontainers.spring.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
class Listener {

    final AtomicReference<String> value = new AtomicReference<>();

    @KafkaListener(topics = KafkaContainerInitializerTest.TOPIC_NAME)
    public void handle(@Payload String value) {
        this.value.set(value);
    }
}
