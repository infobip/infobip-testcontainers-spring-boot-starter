package com.infobip.testcontainers.spring.redis;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("reusable")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class RedisContainerInitializerWithReusableTest {

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
