package com.infobip.testcontainers.spring.rabbit;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("test")
class RabbitContainerInitializerTest extends TestBase {

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
