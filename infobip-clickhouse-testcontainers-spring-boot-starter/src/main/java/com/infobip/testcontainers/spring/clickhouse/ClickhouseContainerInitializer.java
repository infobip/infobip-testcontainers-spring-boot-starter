package com.infobip.testcontainers.spring.clickhouse;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class ClickhouseContainerInitializer extends InitializerBase<ClickhouseContainerWrapper> {

    private static final Pattern JDBC_URL_WITH_DEFINED_PORT_PATTERN = Pattern.compile(".*://.*:(\\d+)(/.*)?");

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

        resolveStaticPort(jdbcUrlValue)
            .ifPresent(staticPort -> container.setPortBindings(Collections.singletonList(staticPort + ":" + ClickhouseContainerWrapper.HTTP_PORT)));

        start(container);
        String url = jdbcUrlValue.replace("<host>", container.getContainerIpAddress())
                                 .replace("<port>", container.getMappedPort(ClickhouseContainerWrapper.HTTP_PORT)
                                                             .toString());
        TestPropertyValues values = TestPropertyValues.of(
            String.format("%s=%s", jdbcUrlPropertyPath, url));
        values.applyTo(applicationContext);
    }

    // todo - unduplicate
    private Optional<Integer> resolveStaticPort(String connectionString) {
        return Optional.ofNullable(connectionString)
                       .map(JDBC_URL_WITH_DEFINED_PORT_PATTERN::matcher)
                       .filter(Matcher::matches)
                       .map(matcher -> matcher.group(1))
                       .map(Integer::valueOf);
    }

}
