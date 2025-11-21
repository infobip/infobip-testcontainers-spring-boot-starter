package com.infobip.testcontainers.spring.kafka;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.concurrent.*;

import static com.infobip.testcontainers.spring.kafka.Listener.TOPIC;
import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

@AllArgsConstructor
@ActiveProfiles("static-port")
class KafkaContainerInitializerWithStaticPortTest extends TestBase {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Listener listener;
    private final KafkaProperties kafkaProperties;

    @Test
    void shouldCreateContainer() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        String givenValue = this.getClass().getName();

        // when
        SendResult<?, ?> actual = kafkaTemplate.send(TOPIC, "key", givenValue)
                                               .get(10, TimeUnit.SECONDS);

        // then
        then(actual).isNotNull();
        await().atMost(Duration.ofSeconds(10))
               .untilAsserted(() -> then(listener.getValue()).isEqualTo(givenValue));
    }

    @Test
    void shouldResolveHostInUrl() {
        // then
        then(kafkaProperties.getBootstrapServers()).containsExactly("localhost:5002");
    }

}
