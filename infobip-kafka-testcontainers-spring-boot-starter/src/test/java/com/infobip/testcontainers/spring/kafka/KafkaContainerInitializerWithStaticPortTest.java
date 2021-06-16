package com.infobip.testcontainers.spring.kafka;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.AllArgsConstructor;
import org.awaitility.Awaitility;
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
        String givenData = "givenData";

        // when
        SendResult<?, ?> actual = kafkaTemplate.send(TOPIC_NAME, "key", givenData)
                                               .completable()
                                               .get(10, TimeUnit.SECONDS);

        // then
        then(actual).isNotNull();
        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> {
            String value = listener.getValue();

            if(Objects.isNull(value)) {
                return false;
            }

            then(value).isEqualTo(givenData);
            return true;
        });
    }

    @Test
    void shouldResolveHostInUrl() {
        // then
        then(kafkaProperties.getBootstrapServers()).containsExactly("localhost:5000");
    }

}
