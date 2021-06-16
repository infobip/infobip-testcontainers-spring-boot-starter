package com.infobip.testcontainers.spring.clickhouse;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class ClickhouseContainerInitializer extends InitializerBase<ClickhouseContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        Optional<String> customPropertyPath = Optional.ofNullable(environment.getProperty("testcontainers.clickhouse.custom-path"));
        String jdbcUrlPropertyPath = customPropertyPath.orElse("spring.datasource") + ".jdbc-url";
        String jdbcUrlValue = Objects.requireNonNull(environment.getProperty(jdbcUrlPropertyPath));
        ClickhouseContainerWrapper container = Optional.ofNullable(
            environment.getProperty("testcontainers.clickhouse.docker.image.version"))
                                                       .map(ClickhouseContainerWrapper::new)
                                                       .orElseGet(ClickhouseContainerWrapper::new);

        resolveStaticPort(jdbcUrlValue, JDBC_URL_WITH_PORT_GROUP_PATTERN)
            .ifPresent(staticPort -> bindPort(container, staticPort, ClickhouseContainerWrapper.HTTP_PORT));

        start(container);

        String url = replaceHostAndPortPlaceholders(jdbcUrlValue,
                                                    container.getContainerIpAddress(),
                                                    container.getMappedPort(ClickhouseContainerWrapper.HTTP_PORT));

        TestPropertyValues values = TestPropertyValues.of(
            String.format("%s=%s", jdbcUrlPropertyPath, url));
        values.applyTo(applicationContext);
    }

}
