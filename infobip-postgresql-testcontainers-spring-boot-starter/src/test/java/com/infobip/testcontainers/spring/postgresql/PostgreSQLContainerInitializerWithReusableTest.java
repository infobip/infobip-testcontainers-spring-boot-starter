package com.infobip.testcontainers.spring.postgresql;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class PostgreSQLContainerInitializerWithReusableTest {

    private final PostgreSQLContainerWrapper wrapper;
    private final int port = PostgreSQLContainer.POSTGRESQL_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new PostgreSQLContainerWrapper("TestDatabase");
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }

}