package com.infobip.testcontainers.spring.redis;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@AllArgsConstructor
@ActiveProfiles("static-port")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
class RedisContainerWithStaticPortTest {

    private final RedisTemplate<String, String> template;
    private final RedisProperties redisProperties;

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

    @Test
    void shouldSetHostInProperties() {
        // then
        then(redisProperties.getUrl()).contains("localhost:5000");
    }

}
