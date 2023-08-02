package com.infobip.testcontainers.spring.clickhouse;

import static com.infobip.testcontainers.spring.clickhouse.DataSourceConfig.CLICKHOUSE_URL_PROPERTY_NAME;
import static org.assertj.core.api.BDDAssertions.then;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@AllArgsConstructor
@ActiveProfiles("static-port")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class ClickhouseContainerInitializerWithStaticPortTest {

    private Environment environment;
    private DataSource dataSource;

    @Test
    void shouldCreateContainerWithStaticPort() {
        //given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //when
        String result = jdbcTemplate.queryForObject("SELECT 1", String.class);

        //then
        then(result).isEqualTo("1");
    }

    @Test
    void shouldResolveHostInUrl() {
        // then
        then(environment.getProperty(CLICKHOUSE_URL_PROPERTY_NAME)).isEqualTo("jdbc:clickhouse://localhost:5001");
    }

}
