package com.infobip.testcontainers.spring.postgresql;

import java.util.Optional;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class PostgreSQLContainerInitializer extends InitializerBase<PostgreSQLContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String dataSourceUrlPropertyName = Optional.ofNullable(
            environment.getProperty("testcontainers.postgresql.datasource.url.property.name"))
                                                   .orElse("spring.datasource.url");
        String dataSourceUrl = environment.getProperty(dataSourceUrlPropertyName);

        if (StringUtils.isEmpty(dataSourceUrl)) {
            throw new IllegalStateException("URL for test-container is null or empty.");
        }

        String database = dataSourceUrl.substring(dataSourceUrl.lastIndexOf("/") + 1);
        PostgreSQLContainerWrapper container = Optional.ofNullable(environment.getProperty("testcontainers.postgresql.docker.image"))
                                                       .map(imageName -> new PostgreSQLContainerWrapper(database, imageName))
                                                       .orElseGet(() -> new PostgreSQLContainerWrapper(database));

        resolveStaticPort(dataSourceUrl, JDBC_URL_WITH_PORT_GROUP_PATTERN)
            .ifPresent(staticPort -> bindPort(container, staticPort, PostgreSQLContainerWrapper.POSTGRESQL_PORT));

        start(container);

        String url = replaceHostAndPortPlaceholders(dataSourceUrl,
                                                    container.getContainerIpAddress(),
                                                    container.getMappedPort(PostgreSQLContainerWrapper.POSTGRESQL_PORT));
        TestPropertyValues values = TestPropertyValues.of(dataSourceUrlPropertyName + "=" + url);

        values.applyTo(applicationContext);
    }

}
