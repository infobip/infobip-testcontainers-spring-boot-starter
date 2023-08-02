package com.infobip.testcontainers.spring.clickhouse;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import javax.sql.DataSource;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("init-script")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class ClickhouseContainerInitializerWithInitScriptTest {

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
