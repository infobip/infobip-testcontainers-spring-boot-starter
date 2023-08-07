package com.infobip.testcontainers.spring.kafka;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.KafkaContainer;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class KafkaContainerInitializerWithReusableTest {

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
