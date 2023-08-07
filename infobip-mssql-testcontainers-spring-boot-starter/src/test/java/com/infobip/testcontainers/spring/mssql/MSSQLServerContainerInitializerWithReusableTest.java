package com.infobip.testcontainers.spring.mssql;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MSSQLServerContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class MSSQLServerContainerInitializerWithReusableTest extends ReusableTestBase {

    private final MSSQLServerContainerWrapper wrapper;
    private final int port = MSSQLServerContainer.MS_SQL_SERVER_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new MSSQLServerContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }
}
