package com.infobip.testcontainers.spring.mssql;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class MSSQLServerContainerReusableTest {

    private final MSSQLServerContainerWrapper mSSQLServerContainerWrapper;

    @Test
    void shouldCreateContainer() {
        // given
        var mssqlServerContainerWrapper = new MSSQLServerContainerWrapper();
        mssqlServerContainerWrapper.withReuse(true);

        // when
        mssqlServerContainerWrapper.start();

        // then
        then(mssqlServerContainerWrapper.getMappedPort(1433)).isEqualTo(mSSQLServerContainerWrapper.getMappedPort(1433));
    }
}
