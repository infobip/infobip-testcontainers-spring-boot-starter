package com.infobip.testcontainers.spring.redis;

import com.infobip.testcontainers.ReusableTestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
class RedisContainerInitializerWithReusableTest extends ReusableTestBase {

    private final RedisContainerWrapper wrapper;
    private final int port = RedisContainerWrapper.PORT;

    @Test
    void shouldReuseContainer() {
        // given
        var givenContainer = new RedisContainerWrapper();
        givenContainer.withReuse(true);

        // when
        givenContainer.start();

        // then
        then(givenContainer.getMappedPort(port)).isEqualTo(wrapper.getMappedPort(port));
    }
}
