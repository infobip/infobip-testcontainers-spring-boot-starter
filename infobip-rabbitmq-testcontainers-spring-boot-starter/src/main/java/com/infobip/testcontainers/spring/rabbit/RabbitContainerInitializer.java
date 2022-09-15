package com.infobip.testcontainers.spring.rabbit;

import static com.infobip.testcontainers.spring.rabbit.RabbitContainerWrapper.PORT;

import java.util.Optional;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.wait.strategy.Wait;

public class RabbitContainerInitializer extends InitializerBase<RabbitContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        Environment environment = configurableApplicationContext.getEnvironment();
        RabbitContainerWrapper container = Optional.ofNullable(
            environment.getProperty("testcontainers.rabbit.docker.image"))
                                                   .map(RabbitContainerWrapper::new)
                                                   .orElseGet(
                                                       () -> new RabbitContainerWrapper("rabbitmq:3.8.9-alpine"));

        resolveStaticPort(environment)
            .ifPresent(staticPort -> bindPort(container, staticPort, PORT));

        container.waitingFor(Wait.forListeningPort());
        start(container);

        TestPropertyValues values = TestPropertyValues.of(
            String.format("spring.rabbitmq.addresses=%s:%d", container.getHost(),
                          container.getMappedPort(PORT)));
        values.applyTo(configurableApplicationContext);
    }

    private Optional<Integer> resolveStaticPort(Environment environment) {
        return Optional.ofNullable(environment.getProperty("spring.rabbitmq.port"))
                       .map(Integer::valueOf);
    }

}
