package com.infobip.testcontainers.spring.kafka;

import com.infobip.testcontainers.InitializerBase;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;

public class KafkaContainerInitializer extends InitializerBase<KafkaContainerWrapper> {

    private static final Pattern KAFKA_SERVER_PATTERN = Pattern.compile("^.*:(\\d+).*");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var environment = applicationContext.getEnvironment();
        var bootstrapServers = Objects.requireNonNull(environment.getProperty("spring.kafka.bootstrap-servers"));
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.kafka.docker.image.version"))
                              .map(KafkaContainerWrapper::new)
                              .orElseGet(KafkaContainerWrapper::new);
        var container = handleReusable(wrapper);

        resolveStaticPort(bootstrapServers, KAFKA_SERVER_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, KafkaContainerWrapper.KAFKA_PORT));

        start(container);
        var url = replaceHostAndPortPlaceholders(bootstrapServers, container, KafkaContainerWrapper.KAFKA_PORT);

        Optional.ofNullable(environment.getProperty("testcontainers.kafka.topics", String[].class))
                .ifPresent(topics -> createTestKafkaTopics(url, topics));
        var values = TestPropertyValues.of(
                "spring.kafka.bootstrap-servers=" + url);
        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }

    private static void createTestKafkaTopics(String bootstrapServers, String[] topics) {

        try (var client = AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers))) {
            deleteTopics(client, topics);
            createTopics(client, topics);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteTopics(AdminClient client, String[] topics) throws InterruptedException,
            ExecutionException, TimeoutException {
        var existingTopics = client.listTopics().names().get(60, TimeUnit.SECONDS);
        var deleteTopics = Stream.of(topics)
                                 .map(topic -> topic.split(":"))
                                 .map(topicParts -> topicParts[0])
                                 .filter(existingTopics::contains)
                                 .collect(Collectors.toList());
        client.deleteTopics(deleteTopics)
              .all()
              .get(60, TimeUnit.SECONDS);
    }

    private static void createTopics(AdminClient client, String[] topics) throws InterruptedException,
            ExecutionException, TimeoutException {
        var newTopics = Stream.of(topics)
                              .map(topic -> topic.split(":"))
                              .map(topicParts -> new NewTopic(topicParts[0], parseInt(topicParts[1]),
                                                              parseShort(topicParts[2])))
                              .collect(Collectors.toList());

        client.createTopics(newTopics)
              .all()
              .get(60, TimeUnit.SECONDS);
    }
}
