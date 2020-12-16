package com.infobip.testcontainers.spring.rabbit;

import lombok.AllArgsConstructor;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestConfiguration
@SpringBootTest(classes = Main.class)
class RabbitContainerInitializerTest {

    private final RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Should initialize rabbit container and send message to exchange")
    void shouldInitialize() {
        // given
        String givenMessage = "test";

        // when
        Throwable actual = BDDAssertions.catchThrowable(
                () -> rabbitTemplate.convertAndSend("test.exchange", "test.key.bar", givenMessage));

        // then
        then(actual).isNull();
    }
}
