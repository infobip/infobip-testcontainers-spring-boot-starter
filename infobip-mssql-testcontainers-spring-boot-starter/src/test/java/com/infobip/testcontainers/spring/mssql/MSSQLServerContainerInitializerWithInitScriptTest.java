package com.infobip.testcontainers.spring.mssql;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("init-script")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class MSSQLServerContainerInitializerWithInitScriptTest {

    private final DataSourceProperties properties;

    @Test
    void shouldCreateDatabase() {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SimpleDriverDataSource(
                getDriver(properties.getUrl()),
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword()));

        // when
        String actual = jdbcTemplate.queryForObject("SELECT TOP 1 name FROM test_table", String.class);

        // then
        then(actual).isEqualTo("Test");
    }

    private Driver getDriver(String jdbcUrl) {
        try {
            return DriverManager.getDriver(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
