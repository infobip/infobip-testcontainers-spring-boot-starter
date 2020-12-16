package com.infobip.testcontainers.spring.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Component
class Listener {

    private final AtomicReference<String> value = new AtomicReference<>();

    @KafkaListener(topics = KafkaContainerInitializerTest.TOPIC_NAME)
    public void handle(@Payload String value) {
        this.value.set(value);
    }

    String getValue() {
        return value.get();
    }
}
