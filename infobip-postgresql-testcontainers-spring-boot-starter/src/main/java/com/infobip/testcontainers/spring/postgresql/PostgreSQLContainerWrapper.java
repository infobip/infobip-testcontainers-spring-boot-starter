package com.infobip.testcontainers.spring.postgresql;

public class PostgreSQLContainerWrapper
        extends org.testcontainers.containers.PostgreSQLContainer<PostgreSQLContainerWrapper> {

    public PostgreSQLContainerWrapper(String databaseName) {
        this(databaseName, IMAGE + ":" + DEFAULT_TAG);
    }

    public PostgreSQLContainerWrapper(String databaseName, String dockerImageName) {
        super(dockerImageName);
        withDatabaseName(databaseName);
    }

}