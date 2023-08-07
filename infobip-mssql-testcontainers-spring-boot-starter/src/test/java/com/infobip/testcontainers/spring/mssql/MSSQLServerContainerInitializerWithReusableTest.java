package com.infobip.testcontainers.spring.mssql;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.MSSQLServerContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class MSSQLServerContainerInitializerWithReusableTest {

    private final MSSQLServerContainerWrapper container;
    private final int port = MSSQLServerContainer.MS_SQL_SERVER_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new MSSQLServerContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(container.getMappedPort(port));
    }
}
