package com.infobip.testcontainers.spring.rabbit;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Optional;

import static com.infobip.testcontainers.spring.rabbit.RabbitContainerWrapper.PORT;

public class RabbitContainerInitializer extends InitializerBase<RabbitContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        Environment environment = configurableApplicationContext.getEnvironment();
        RabbitContainerWrapper container = Optional.ofNullable(
                environment.getProperty("testcontainers.rabbit.docker.image"))
                                                   .map(RabbitContainerWrapper::new)
                                                   .orElseGet(() -> new RabbitContainerWrapper("rabbitmq:3.8.9-alpine"));

        container.waitingFor(Wait.forListeningPort());
        start(container);

        TestPropertyValues values = TestPropertyValues.of(
                String.format("spring.rabbitmq.addresses=%s:%d", container.getContainerIpAddress(),
                              container.getMappedPort(PORT)));
        values.applyTo(configurableApplicationContext);
    }
}
