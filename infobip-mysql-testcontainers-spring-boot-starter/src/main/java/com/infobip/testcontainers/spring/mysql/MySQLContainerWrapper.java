package com.infobip.testcontainers.spring.mysql;

public class MySQLContainerWrapper extends org.testcontainers.containers.MySQLContainer<MySQLContainerWrapper> {

    public MySQLContainerWrapper(String databaseName) {
        this(databaseName, IMAGE + ":" + DEFAULT_TAG);
    }

    public MySQLContainerWrapper(String databaseName, String dockerImageName) {
        super(dockerImageName);
        withDatabaseName(databaseName);
    }

}