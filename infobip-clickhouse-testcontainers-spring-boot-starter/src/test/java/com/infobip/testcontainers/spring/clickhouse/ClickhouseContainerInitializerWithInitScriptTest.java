package com.infobip.testcontainers.spring.clickhouse;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("init-script")
class ClickhouseContainerInitializerWithInitScriptTest  extends TestBase {

    private DataSource dataSource;

    @Test
    void shouldCreateContainer() {
        //given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //when
        String result = jdbcTemplate.queryForObject("SELECT name FROM test_table WHERE id = 1", String.class);

        //then
        then(result).isEqualTo("Test");
    }

}
