package com.infobip.testcontainers.spring.rabbit;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class RabbitContainerInitializerWithReusableTest extends ReusableTestBase {

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
