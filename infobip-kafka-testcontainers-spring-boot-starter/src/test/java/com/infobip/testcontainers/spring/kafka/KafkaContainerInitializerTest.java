package com.infobip.testcontainers.spring.kafka;

import lombok.AllArgsConstructor;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class KafkaContainerInitializerTest {

    final static String TOPIC_NAME = "test-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Listener listener;

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
        Awaitility.await().atMost(Duration.ofSeconds(30)).until(() -> {
            String value = listener.getValue();

            if(Objects.isNull(value)) {
                return false;
            }

            then(value).isEqualTo(givenData);
            return true;
        });
    }
}
