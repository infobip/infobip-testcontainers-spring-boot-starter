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
        var environment = applicationContext.getEnvironment();
        var redisUrl = Objects.requireNonNull(environment.getProperty("spring.data.redis.url"));
        var wrapper = Optional.ofNullable(environment.getProperty("testcontainers.redis.docker.image"))
                              .map(RedisContainerWrapper::new)
                              .orElseGet(() -> new RedisContainerWrapper("redis:6.2.6-alpine"));
        var container = handleReusable(environment, wrapper);
        resolveStaticPort(redisUrl, GENERIC_URL_WITH_PORT_GROUP_PATTERN)
                .ifPresent(staticPort -> bindPort(container, staticPort, RedisContainerWrapper.PORT));

        start(container);

        var url = replaceHostAndPortPlaceholders(redisUrl, container, RedisContainerWrapper.PORT);
        var values = TestPropertyValues.of("spring.data.redis.url=" + url);
        values.applyTo(applicationContext);

        registerContainerAsBean(applicationContext);
    }
}
