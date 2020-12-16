package com.infobip.testcontainers.spring.kafka;

import com.infobip.testcontainers.InitializerBase;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;

public class KafkaContainerInitializer extends InitializerBase<KafkaContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String bootstrapServers = Objects.requireNonNull(environment.getProperty("spring.kafka.bootstrap-servers"));
        KafkaContainerWrapper container = Optional.ofNullable(
                environment.getProperty("testcontainers.kafka.docker.image.version"))
                                                  .map(KafkaContainerWrapper::new)
                                                  .orElseGet(KafkaContainerWrapper::new);

        start(container);
        String url = bootstrapServers.replace("<host>", container.getContainerIpAddress())
                                     .replace("<port>", container.getMappedPort(KafkaContainerWrapper.KAFKA_PORT)
                                                                 .toString());
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
