package com.infobip.testcontainers.spring.clickhouse;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.yandex.clickhouse.ClickHouseDriver;

@Configuration
public class DataSourceConfig {

    static final String CLICKHOUSE_URL_PROPERTY_NAME = "spring.datasource.clickhouse.jdbc-url";

    @Bean
    public DataSource getDataSource(Environment environment) {
        return DataSourceBuilder.create()
                                .driverClassName(ClickHouseDriver.class.getName())
                                .url(environment.getProperty(CLICKHOUSE_URL_PROPERTY_NAME))
                                .build();
    }

}
