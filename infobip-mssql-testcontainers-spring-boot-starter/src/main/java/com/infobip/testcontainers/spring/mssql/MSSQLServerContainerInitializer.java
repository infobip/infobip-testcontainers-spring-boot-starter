package com.infobip.testcontainers.spring.mssql;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.stream.Collectors;

import static org.testcontainers.containers.MSSQLServerContainer.MS_SQL_SERVER_PORT;

public class MSSQLServerContainerInitializer extends InitializerBase<MSSQLServerContainerWrapper> {

    private static final List<String> DEFAULT_PROPERTY_NAMES = Arrays.asList("spring.datasource.url",
                                                                             "spring.flyway.url",
                                                                             "spring.r2dbc.url");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        List<String> urlPropertyNames = getUrlPropertyNames(environment);
        Map<String, String> urlPropertyNameToValue = getUrlPropertyNameToValue(environment, urlPropertyNames);

        MSSQLServerContainerWrapper container = Optional.ofNullable(
                environment.getProperty("testcontainers.mssql.docker.image"))
                                                        .map(MSSQLServerContainerWrapper::new)
                                                        .orElseGet(MSSQLServerContainerWrapper::new);
        start(container);
        Map<String, String> replacedNameToValue = replaceHostAndPort(urlPropertyNameToValue, container);
        Map<String, String> testPropertyValues = addMissingUsernameAndPassword(replacedNameToValue, container);
        TestPropertyValues values = TestPropertyValues.of(testPropertyValues);

        values.applyTo(applicationContext);
        String url = replacedNameToValue.getOrDefault("spring.datasource.url",
                                                      replacedNameToValue.get("spring.flyway.url"));
        DatabaseCreator creator = new DatabaseCreator(url, container.getUsername(), container.getPassword());
        creator.createDatabaseIfItDoesntExist();
    }

    private List<String> getUrlPropertyNames(Environment environment) {

        String name = environment.getProperty("testcontainers.mssql.datasource.url.property.name");

        if (Objects.nonNull(name)) {
            return Collections.singletonList(name);
        }

        String[] names = environment.getProperty("testcontainers.mssql.datasource.url.property.names", String[].class);

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
                                                   MSSQLServerContainerWrapper container) {
        String host = container.getContainerIpAddress();
        String port = container.getMappedPort(MS_SQL_SERVER_PORT).toString();
        return urlPropertyNameToValue.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(Map.Entry::getKey,
                                                               entry -> entry.getValue().replace("<host>", host)
                                                                             .replace("<port>", port)));
    }

    private Map<String, String> addMissingUsernameAndPassword(Map<String, String> urlPropertyNameToValue,
                                                              MSSQLServerContainerWrapper container) {
        String username = container.getUsername();
        String password = container.getPassword();

        Map<String, String> testPropertyValues = new HashMap<>(urlPropertyNameToValue);
        for (Map.Entry<String, String> entry : urlPropertyNameToValue.entrySet()) {
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
