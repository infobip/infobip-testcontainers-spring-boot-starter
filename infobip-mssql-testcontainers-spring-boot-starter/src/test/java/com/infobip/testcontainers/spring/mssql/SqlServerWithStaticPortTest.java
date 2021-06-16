package com.infobip.testcontainers.spring.mssql;

import static org.assertj.core.api.BDDAssertions.then;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.Testcontainers;

@AllArgsConstructor
@ActiveProfiles("static-port")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class SqlServerWithStaticPortTest {

    private final Environment environment;
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
        String actual = jdbcTemplate.queryForObject("SELECT db_name()", String.class);

        // then
        then(actual).isEqualTo("MicrosoftDriverTestDatabase");
    }

    @Test
    void shouldReplaceHostInJdbcUrl() {
        // when
        String actual = environment.getProperty("spring.datasource.url");

        // then
        then(actual).contains("localhost:1433");
    }

    @Test
    void shouldReplaceHostInR2dbc() {
        // when
        String actual = environment.getProperty("spring.r2dbc.url");

        // then
        then(actual).contains("localhost:1433");
    }

    @Test
    void shouldReplaceHostInFlyway() {
        // when
        String actual = environment.getProperty("spring.flyway.url");

        // then
        then(actual).contains("localhost:1433");
    }

    private Driver getDriver(String jdbcUrl) {
        try {
            return DriverManager.getDriver(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
