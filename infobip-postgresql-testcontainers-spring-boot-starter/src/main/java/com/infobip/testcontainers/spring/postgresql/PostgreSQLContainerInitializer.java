package com.infobip.testcontainers.spring.postgresql;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class PostgreSQLContainerInitializer extends InitializerBase<PostgreSQLContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var environment = applicationContext.getEnvironment();
        var dataSourceUrlPropertyName = Optional.ofNullable(
                                                        environment.getProperty("testcontainers.postgresql.datasource.url.property.name"))
                                                .orElse("spring.datasource.url");
        var dataSourceUrl = environment.getProperty(dataSourceUrlPropertyName);

        if (StringUtils.isEmpty(dataSourceUrl)) {
            throw new IllegalStateException("URL for test-container is null or empty.");
        }

        var database = dataSourceUrl.substring(dataSourceUrl.lastIndexOf("/") + 1);
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.postgresql.docker.image"))
                              .map(imageName -> new PostgreSQLContainerWrapper(database, imageName))
                              .orElseGet(() -> new PostgreSQLContainerWrapper(database));
        var container = handleReusable(environment, wrapper);

        Optional.ofNullable(environment.getProperty("testcontainers.postgresql.init-script"))
                .ifPresent(container::withInitScript);

        resolveStaticPort(dataSourceUrl, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, PostgreSQLContainerWrapper.POSTGRESQL_PORT));

        start(container);

        var url = replaceHostAndPortPlaceholders(dataSourceUrl, container, PostgreSQLContainerWrapper.POSTGRESQL_PORT);
        var values = TestPropertyValues.of(dataSourceUrlPropertyName + "=" + url);

        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }
}
