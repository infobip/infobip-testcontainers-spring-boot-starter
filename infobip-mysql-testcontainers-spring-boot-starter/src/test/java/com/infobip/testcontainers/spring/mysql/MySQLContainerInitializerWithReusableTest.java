package com.infobip.testcontainers.spring.mysql;

import com.infobip.testcontainers.ReusableTestBase;
import com.infobip.testcontainers.spring.mysql.MySQLContainerWrapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class MySQLContainerInitializerWithReusableTest extends ReusableTestBase {

    private final MySQLContainerWrapper wrapper;
    private final int port = MySQLContainerWrapper.MYSQL_PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new MySQLContainerWrapper("TestDatabase");
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }

}