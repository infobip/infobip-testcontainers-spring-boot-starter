package com.infobip.testcontainers.spring.mssql;

public class MSSQLServerContainerWrapper
        extends org.testcontainers.containers.MSSQLServerContainer<MSSQLServerContainerWrapper> {

    public MSSQLServerContainerWrapper() {
        super(IMAGE + ":" + DEFAULT_TAG);
    }

    public MSSQLServerContainerWrapper(String image) {
        super(image);
    }

    @Override
    protected void configure() {
        addEnv("ACCEPT_EULA", "Y");
        addEnv("SA_PASSWORD", getPassword());
    }
}
