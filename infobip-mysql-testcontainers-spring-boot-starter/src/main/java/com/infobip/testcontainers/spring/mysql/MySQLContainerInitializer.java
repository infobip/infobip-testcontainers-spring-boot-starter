package com.infobip.testcontainers.spring.mysql;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class MySQLContainerInitializer extends InitializerBase<MySQLContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var environment = applicationContext.getEnvironment();
        var dataSourceUrlPropertyName = Optional.ofNullable(
                                                        environment.getProperty("testcontainers.mysql.datasource.url.property.name"))
                                                .orElse("spring.datasource.url");
        var dataSourceUrl = environment.getProperty(dataSourceUrlPropertyName);

        if (StringUtils.isEmpty(dataSourceUrl)) {
            throw new IllegalStateException("URL for test-container is null or empty.");
        }

        var database = dataSourceUrl.substring(dataSourceUrl.lastIndexOf("/") + 1);
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.mysql.docker.image"))
                              .map(imageName -> new MySQLContainerWrapper(database, imageName))
                              .orElseGet(() -> new MySQLContainerWrapper(database));
        var container = handleReusable(wrapper);

        Optional.ofNullable(environment.getProperty("testcontainers.mysql.init-script"))
                .ifPresent(container::withInitScript);

        resolveStaticPort(dataSourceUrl, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, MySQLContainerWrapper.MYSQL_PORT));

        start(container);

        var url = replaceHostAndPortPlaceholders(dataSourceUrl, container, MySQLContainerWrapper.MYSQL_PORT);
        var values = TestPropertyValues.of(dataSourceUrlPropertyName + "=" + url);

        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }
}
