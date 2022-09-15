package com.infobip.testcontainers.spring.rabbit;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@AllArgsConstructor
@ActiveProfiles("static-port")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestConfiguration
@SpringBootTest(classes = Main.class)
class RabbitContainerInitializerWithStaticPortTest {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    @Test
    @DisplayName("Should initialize rabbit container and send message to exchange")
    void shouldInitialize() {
        // given
        String givenMessage = "test";

        // when
        rabbitTemplate.convertAndSend("test.exchange", "test.key.bar", givenMessage);

        // then
//        then(actual).isNull();
    }

    @Test
    void shouldUseStaticPort() {
        // then
        then(rabbitProperties.getAddresses()).contains("localhost:5005");
    }

}
