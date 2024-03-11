package com.infobip.testcontainers.spring.mysql;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.stream.Collectors;

import static org.testcontainers.containers.MySQLContainer.MYSQL_PORT;

public class MySQLContainerInitializer extends InitializerBase<MySQLContainerWrapper> {

    private static final List<String> DEFAULT_PROPERTY_NAMES = Arrays.asList("spring.datasource.url",
                                                                             "spring.flyway.url",
                                                                             "spring.r2dbc.url");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var environment = applicationContext.getEnvironment();
        var urlPropertyNames = getUrlPropertyNames(environment);
        var urlPropertyNameToValue = getUrlPropertyNameToValue(environment, urlPropertyNames);
        var dataSourceUrlPropertyName = Optional.ofNullable(
                                                        environment.getProperty("testcontainers.mysql.datasource.url.property.name"))
                                                .orElse("spring.datasource.url");
        var dataSourceUrl = Objects.requireNonNull(environment.getProperty(dataSourceUrlPropertyName));
        var database = dataSourceUrl.substring(dataSourceUrl.lastIndexOf("/") + 1);
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.mysql.docker.image"))
                              .map(imageName -> new MySQLContainerWrapper(database, imageName))
                              .orElseGet(() -> new MySQLContainerWrapper(database));
        var container = handleReusable(wrapper);

        Optional.ofNullable(environment.getProperty("testcontainers.mysql.init-script"))
                .ifPresent(container::withInitScript);

        resolveStaticPort(dataSourceUrl, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, MYSQL_PORT));

        start(container);

        var replacedNameToValue = replaceHostAndPort(urlPropertyNameToValue, container);
        var testPropertyValues = addMissingUsernameAndPassword(replacedNameToValue, container);
        var values = TestPropertyValues.of(testPropertyValues);

        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }

    private List<String> getUrlPropertyNames(Environment environment) {
        var name = environment.getProperty("testcontainers.mysql.datasource.url.property.name");
        if (Objects.nonNull(name)) {
            return Collections.singletonList(name);
        }

        var names = environment.getProperty("testcontainers.mysql.datasource.url.property.names", String[].class);
        if (Objects.nonNull(names)) {
            return Arrays.asList(names);
        }

        return DEFAULT_PROPERTY_NAMES;
    }

    private Map<String, String> getUrlPropertyNameToValue(Environment environment, List<String> names) {
        Map<String, String> propertyNameToValue = new HashMap<>();

        for (String name : names) {
            String value = environment.getProperty(name);
            if (Objects.nonNull(value)) {
                propertyNameToValue.put(name, value);
            }
        }

        return propertyNameToValue;
    }

    private Map<String, String> replaceHostAndPort(Map<String, String> urlPropertyNameToValue,
                                                   MySQLContainerWrapper container) {
        return urlPropertyNameToValue.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(Map.Entry::getKey,
                                                               entry -> replaceHostAndPortPlaceholders(entry.getValue(),
                                                                                                       container,
                                                                                                       MYSQL_PORT)));
    }

    private Map<String, String> addMissingUsernameAndPassword(Map<String, String> urlPropertyNameToValue,
                                                              MySQLContainerWrapper container) {
        var username = container.getUsername();
        var password = container.getPassword();

        var testPropertyValues = new HashMap<>(urlPropertyNameToValue);
        for (var entry : urlPropertyNameToValue.entrySet()) {
            String name = entry.getKey();
            if (DEFAULT_PROPERTY_NAMES.contains(name)) {
                String root = name.substring(0, name.indexOf(".url"));
                testPropertyValues.put(root + ".username", username);
                testPropertyValues.put(root + ".password", password);
            }
        }
        return testPropertyValues;
    }
}
