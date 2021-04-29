package com.infobip.testcontainers.spring.clickhouse;

import org.testcontainers.containers.ClickHouseContainer;

public class ClickhouseContainerWrapper extends ClickHouseContainer {

    public ClickhouseContainerWrapper() {
    }

    public ClickhouseContainerWrapper(String confluentPlatformVersion) {
        super(confluentPlatformVersion);
    }
}
