package com.infobip.testcontainers.spring.mssql;

import org.testcontainers.utility.DockerImageName;

public class MSSQLServerContainerWrapper
        extends org.testcontainers.containers.MSSQLServerContainer<MSSQLServerContainerWrapper> {

    @Deprecated
    public MSSQLServerContainerWrapper() {
        super();
    }

    public MSSQLServerContainerWrapper(String image) {
        super(image);
    }

    public MSSQLServerContainerWrapper(DockerImageName dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected void configure() {
        addEnv("ACCEPT_EULA", "Y");
        addEnv("SA_PASSWORD", getPassword());
    }
}
