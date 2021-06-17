package com.infobip.testcontainers.spring.kafka;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@AllArgsConstructor
@ActiveProfiles("static-port")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class KafkaContainerInitializerWithStaticPortTest {

    final static String TOPIC_NAME = "test-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Listener listener;
    private final KafkaProperties kafkaProperties;

    @Test
    void shouldCreateContainer() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        String givenValue = this.getClass().getName();

        // when
        SendResult<?, ?> actual = kafkaTemplate.send(TOPIC_NAME, "key", givenValue)
                                               .completable()
                                               .get(30, TimeUnit.SECONDS);

        // then
        then(actual).isNotNull();
        await().atMost(Duration.ofSeconds(10))
               .untilAsserted(() -> then(listener.getValue()).isEqualTo(givenValue));
    }

    @Test
    void shouldResolveHostInUrl() {
        // then
        then(kafkaProperties.getBootstrapServers()).containsExactly("localhost:5001");
    }

}
