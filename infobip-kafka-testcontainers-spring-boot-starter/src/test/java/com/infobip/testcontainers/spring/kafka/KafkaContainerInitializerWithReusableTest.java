package com.infobip.testcontainers.spring.kafka;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class KafkaContainerInitializerWithReusableTest extends ReusableTestBase {

    private final KafkaContainerWrapper wrapper;
    private final int port = KafkaContainer.KAFKA_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new KafkaContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }

}
