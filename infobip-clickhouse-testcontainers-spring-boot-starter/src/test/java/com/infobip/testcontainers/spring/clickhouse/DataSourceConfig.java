package com.infobip.testcontainers.spring.clickhouse;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AllArgsConstructor
public class DataSourceConfig {

    private final Environment environment;

    @Bean
    public DataSource getDataSource() {
        String url = environment.getProperty("spring.datasource.clickhouse.jdbc-url");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        dataSourceBuilder.url(url);
        return dataSourceBuilder.build();
    }
}
