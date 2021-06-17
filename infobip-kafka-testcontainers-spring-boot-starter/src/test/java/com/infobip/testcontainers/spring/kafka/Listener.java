package com.infobip.testcontainers.spring.kafka;

import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
class Listener {

    public static final String TOPIC = "test-topic";

    private final AtomicReference<String> value = new AtomicReference<>();

    @KafkaListener(topics = TOPIC)
    public void handle(@Payload String value) {
        this.value.set(value);
    }

    String getValue() {
        return value.get();
    }

}
