package com.infobip.testcontainers.spring.mysql;

import static org.assertj.core.api.BDDAssertions.then;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;

@AllArgsConstructor
@ActiveProfiles("static-port")
class MySQLContainerInitializerWithStaticPortTest extends TestBase {

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
        String actual = jdbcTemplate.queryForObject("SELECT database();", String.class);

        // then
        then(actual).isEqualTo("TestDatabase");
    }

    @Test
    void shouldReplaceHostInJdbcUrl() {
        // when
        String actual = properties.getUrl();

        // then
        then(actual).contains("localhost:5005");
    }

    private Driver getDriver(String jdbcUrl) {
        try {
            return DriverManager.getDriver(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
