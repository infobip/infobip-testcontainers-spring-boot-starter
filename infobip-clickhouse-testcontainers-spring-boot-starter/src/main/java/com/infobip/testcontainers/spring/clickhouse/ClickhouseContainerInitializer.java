package com.infobip.testcontainers.spring.clickhouse;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.Optional;

public class ClickhouseContainerInitializer extends InitializerBase<ClickhouseContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var environment = applicationContext.getEnvironment();
        var customPropertyPath = Optional.ofNullable(environment.getProperty("testcontainers.clickhouse.custom-path"));
        var jdbcUrlPropertyPath = customPropertyPath.orElse("spring.datasource") + ".jdbc-url";
        var jdbcUrlValue = Objects.requireNonNull(environment.getProperty(jdbcUrlPropertyPath));
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.clickhouse.docker.image.version"))
                              .map(ClickhouseContainerWrapper::new)
                              .orElseGet(ClickhouseContainerWrapper::new);
        var container = handleReusable(environment, wrapper);

        Optional.ofNullable(environment.getProperty("testcontainers.clickhouse.init-script"))
                .ifPresent(container::withInitScript);

        resolveStaticPort(jdbcUrlValue, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, ClickhouseContainerWrapper.HTTP_PORT));

        start(container);

        var url = replaceHostAndPortPlaceholders(jdbcUrlValue, container, ClickhouseContainerWrapper.HTTP_PORT);

        var values = TestPropertyValues.of(String.format("%s=%s", jdbcUrlPropertyPath, url));
        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }
}
