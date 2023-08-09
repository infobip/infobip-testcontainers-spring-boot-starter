package com.infobip.testcontainers.spring.postgresql;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class PostgreSQLContainerInitializerWithReusableTest extends ReusableTestBase {

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