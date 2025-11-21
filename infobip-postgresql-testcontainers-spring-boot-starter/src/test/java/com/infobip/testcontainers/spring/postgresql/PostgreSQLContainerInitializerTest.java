package com.infobip.testcontainers.spring.postgresql;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;

import java.sql.*;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("test")
class PostgreSQLContainerInitializerTest extends TestBase {

    private final DataSourceProperties properties;

    @Test
    void shouldCreateContainer() {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SimpleDriverDataSource(
                getDriver(properties.getUrl()),
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword()));

        // when
        String actual = jdbcTemplate.queryForObject("SELECT current_database();", String.class);

        // then
        then(actual).isEqualTo("TestDatabase");
    }

    private Driver getDriver(String jdbcUrl) {
        try {
            return DriverManager.getDriver(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}