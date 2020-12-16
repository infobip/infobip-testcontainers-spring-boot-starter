package com.infobip.testcontainers.spring.mssql;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static org.testcontainers.containers.MSSQLServerContainer.MS_SQL_SERVER_PORT;

public class MSSQLServerContainerInitializer extends InitializerBase<MSSQLServerContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String dataSourceUrlPropertyName = Optional.ofNullable(
                environment.getProperty("testcontainers.mssql.datasource.url.property.name"))
                                                   .orElse("spring.datasource.url");
        String dataSourceUrl = environment.getProperty(dataSourceUrlPropertyName);

        if (StringUtils.isEmpty(dataSourceUrl)) {
            throw new IllegalStateException("URL for test-container is null or empty.");
        }

        MSSQLServerContainerWrapper container = Optional.ofNullable(
                environment.getProperty("testcontainers.mssql.docker.image"))
                                                        .map(MSSQLServerContainerWrapper::new)
                                                        .orElseGet(MSSQLServerContainerWrapper::new);
        start(container);
        String url = dataSourceUrl.replace("<host>", container.getContainerIpAddress())
                                  .replace("<port>", container.getMappedPort(MS_SQL_SERVER_PORT).toString());

        TestPropertyValues values = TestPropertyValues.of(dataSourceUrlPropertyName + "=" + url);

        values.applyTo(applicationContext);
        DatabaseCreator creator = new DatabaseCreator(url, container.getUsername(), container.getPassword());
        creator.createDatabaseIfItDoesntExist();
    }
}
