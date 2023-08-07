package com.infobip.testcontainers.spring.redis;

import com.infobip.testcontainers.TestBase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
@ActiveProfiles("test")
class RedisContainerInitializerTest extends TestBase {

    private final RedisTemplate<String, String> template;

    @Test
    void shouldCreateContainer() {

        // given
        String givenKey = "givenKey";
        String givenHashKey = "givenHashKey";
        String givenData = "givenData";
        template.opsForHash().put(givenKey, givenHashKey, givenData);

        // when
        Object actual = template.opsForHash().get(givenKey, givenHashKey);

        // then
        then(actual).isEqualTo(givenData);
    }
}
