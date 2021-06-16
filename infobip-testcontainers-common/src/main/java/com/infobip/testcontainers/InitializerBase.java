package com.infobip.testcontainers;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.lifecycle.Startable;

public abstract class InitializerBase<C extends Startable>
    implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener<ContextClosedEvent> {

    public static final String PORT_PLACEHOLDER = "<port>";
    public static final String HOST_PLACEHOLDER = "<host>";

    protected static final Pattern JDBC_URL_WITH_PORT_GROUP_PATTERN = Pattern.compile(".*://.*:(\\d+)(/.*)?");

    private final AtomicReference<C> container = new AtomicReference<>();

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        C value = container.get();
        if (value != null) {
            value.stop();
        }
    }

    protected void start(C container) {
        this.container.set(container);
        container.start();
    }

    protected String replaceHostAndPortPlaceholders(String source, String host, Integer port) {
        return source.replace(HOST_PLACEHOLDER, host)
                     .replace(PORT_PLACEHOLDER, port.toString());
    }

    protected Optional<Integer> resolveStaticPort(String connectionString, Pattern urlPatternWithPortGroup) {
        return Optional.ofNullable(connectionString)
                       .map(urlPatternWithPortGroup::matcher)
                       .filter(Matcher::matches)
                       .map(matcher -> matcher.group(1))
                       .map(Integer::valueOf);
    }

    protected Optional<Integer> resolveStaticPort(Collection<String> connectionStrings, Pattern urlPatternWithPortGroup) {
        return connectionStrings.stream()
                                .flatMap(connectionString -> resolveStaticPort(connectionString, urlPatternWithPortGroup)
                                    .map(Stream::of)
                                    .orElseGet(Stream::empty))
                                .findFirst();
    }

}
