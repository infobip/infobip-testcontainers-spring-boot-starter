package com.infobip.testcontainers.spring.rabbit;

import lombok.AllArgsConstructor;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestConfiguration
@SpringBootTest(classes = Main.class)
class RabbitContainerInitializerWithReusableTest {

    private final RabbitContainerWrapper wrapper;
    private final int port = RabbitContainerWrapper.PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new RabbitContainerWrapper("rabbitmq:3.6.14-alpine");
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }

}
