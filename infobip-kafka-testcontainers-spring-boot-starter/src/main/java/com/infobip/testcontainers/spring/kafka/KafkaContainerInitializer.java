package com.infobip.testcontainers.spring.kafka;

import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.infobip.testcontainers.InitializerBase;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class KafkaContainerInitializer extends InitializerBase<KafkaContainerWrapper> {

    private static final Pattern KAFKA_SERVER_PATTERN = Pattern.compile("^.*:(\\d+).*");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String bootstrapServers = Objects.requireNonNull(environment.getProperty("spring.kafka.bootstrap-servers"));
        KafkaContainerWrapper container = Optional.ofNullable(
            environment.getProperty("testcontainers.kafka.docker.image.version"))
                                                  .map(KafkaContainerWrapper::new)
                                                  .orElseGet(KafkaContainerWrapper::new);

        resolveStaticPort(bootstrapServers, KAFKA_SERVER_PATTERN)
            .ifPresent(staticPort -> bindPort(container, staticPort, KafkaContainerWrapper.KAFKA_PORT));

        start(container);
        String url = replaceHostAndPortPlaceholders(bootstrapServers,
                                                    container.getContainerIpAddress(),
                                                    container.getMappedPort(KafkaContainerWrapper.KAFKA_PORT));

        Optional.ofNullable(environment.getProperty("testcontainers.kafka.topics", String[].class))
                .ifPresent(topics -> createTestKafkaTopics(url, topics));
        TestPropertyValues values = TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=" + url);
        values.applyTo(applicationContext);
    }

    private static void createTestKafkaTopics(String bootstrapServers, String[] topics) {

        List<NewTopic> newTopics = Stream.of(topics)
                                         .map(topic -> topic.split(":"))
                                         .map(topicParts -> new NewTopic(topicParts[0], parseInt(topicParts[1]),
                                                                         parseShort(topicParts[2])))
                                         .collect(Collectors.toList());

        try {
            AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers))
                       .createTopics(newTopics)
                       .all()
                       .get(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
