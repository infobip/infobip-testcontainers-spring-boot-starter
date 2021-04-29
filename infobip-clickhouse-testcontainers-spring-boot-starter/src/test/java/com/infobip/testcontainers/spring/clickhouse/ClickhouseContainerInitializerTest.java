package com.infobip.testcontainers.spring.clickhouse;

import static org.assertj.core.api.BDDAssertions.then;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@AllArgsConstructor
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class ClickhouseContainerInitializerTest {

    DataSource dataSource;

    @Test
    void shouldCreateContainer()  {
        //given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //when
        String result = jdbcTemplate.queryForObject("SELECT 1", String.class);

        //then
        then(result).isEqualTo("1");
    }
}
