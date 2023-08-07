package com.infobip.testcontainers.spring.clickhouse;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.ClickHouseContainer;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class ClickhouseContainerInitializerWithReusableTest {

    private final ClickhouseContainerWrapper clickhouseContainerWrapper;
    private final int port = ClickHouseContainer.NATIVE_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new ClickhouseContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(clickhouseContainerWrapper.getMappedPort(port));
    }

}
