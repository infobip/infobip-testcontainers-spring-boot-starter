package com.infobip.testcontainers.spring.clickhouse;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.ClickHouseContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class ClickhouseContainerInitializerWithReusableTest  extends ReusableTestBase {

    private final ClickhouseContainerWrapper wrapper;
    private final int port = ClickHouseContainer.NATIVE_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new ClickhouseContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }

}
