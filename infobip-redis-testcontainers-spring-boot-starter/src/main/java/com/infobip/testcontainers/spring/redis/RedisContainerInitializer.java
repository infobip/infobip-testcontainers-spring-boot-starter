package com.infobip.testcontainers.spring.redis;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.Optional;

import static com.infobip.testcontainers.spring.redis.RedisContainerWrapper.PORT;

public class RedisContainerInitializer extends InitializerBase<RedisContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String redisUrl = Objects.requireNonNull(environment.getProperty("spring.redis.url"));
        RedisContainerWrapper container = Optional.ofNullable(
                environment.getProperty("testcontainers.redis.docker.image"))
                                                  .map(RedisContainerWrapper::new)
                                                  .orElseGet(() -> new RedisContainerWrapper("redis:5.0.7-alpine"));
        start(container);
        String url = redisUrl.replace("<host>", container.getContainerIpAddress())
                             .replace("<port>", container.getMappedPort(PORT).toString());
        TestPropertyValues values = TestPropertyValues.of("spring.redis.url=" + url);
        values.applyTo(applicationContext);
    }
}
