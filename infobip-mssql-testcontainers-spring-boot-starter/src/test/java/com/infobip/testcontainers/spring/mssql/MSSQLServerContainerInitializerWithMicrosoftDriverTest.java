package com.infobip.testcontainers.spring.mssql;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.sql.*;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("microsoft-driver")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class MSSQLServerContainerInitializerWithMicrosoftDriverTest {

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
    void shouldInsertUsername() {
        // when
        String actual = properties.getUsername();

        // then
        then(actual).isNotBlank();
    }

    @Test
    void shouldInsertPassword() {
       // when
        String actual = properties.getPassword();

        // then
        then(actual).isNotBlank();
    }

    @Test
    void shouldReplaceHostAndPortInR2dbc() {
        // when
        String actual = environment.getProperty("spring.r2dbc.url");

        // then
        then(actual).isNotBlank()
                    .doesNotContain("<host>")
                    .doesNotContain("<port>");
    }

    @Test
    void shouldInsertUsernameInR2dbc() {
        // when
        String actual = environment.getProperty("spring.r2dbc.username");

        // then
        then(actual).isNotBlank();
    }

    @Test
    void shouldInsertPasswordInR2dbc() {
        // when
        String actual = environment.getProperty("spring.r2dbc.password");

        // then
        then(actual).isNotBlank();
    }

    @Test
    void shouldReplaceHostAndPortInFlyway() {
        // when
        String actual = environment.getProperty("spring.flyway.url");

        // then
        then(actual).isNotBlank()
                    .doesNotContain("<host>")
                    .doesNotContain("<port>");
    }

    @Test
    void shouldInsertUsernameInFlyway() {
        // when
        String actual = environment.getProperty("spring.flyway.username");

        // then
        then(actual).isNotBlank();
    }

    @Test
    void shouldInsertPasswordInFlyway() {
        // when
        String actual = environment.getProperty("spring.flyway.password");

        // then
        then(actual).isNotBlank();
    }

    private Driver getDriver(String jdbcUrl) {
        try {
            return DriverManager.getDriver(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
