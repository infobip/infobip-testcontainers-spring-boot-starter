package com.infobip.testcontainers.spring.redis;

import java.util.Objects;
import java.util.Optional;

import com.infobip.testcontainers.InitializerBase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class RedisContainerInitializer extends InitializerBase<RedisContainerWrapper> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String redisUrl = Objects.requireNonNull(environment.getProperty("spring.redis.url"));
        RedisContainerWrapper container = Optional.ofNullable(
            environment.getProperty("testcontainers.redis.docker.image"))
                                                  .map(RedisContainerWrapper::new)
                                                  .orElseGet(() -> new RedisContainerWrapper("redis:6.2.6-alpine"));
        resolveStaticPort(redisUrl, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
            .ifPresent(staticPort -> bindPort(container, staticPort, RedisContainerWrapper.PORT));

        start(container);

        String url = replaceHostAndPortPlaceholders(redisUrl, container, RedisContainerWrapper.PORT);
        TestPropertyValues values = TestPropertyValues.of("spring.redis.url=" + url);
        values.applyTo(applicationContext);
    }

}
