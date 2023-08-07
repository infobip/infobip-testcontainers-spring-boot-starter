package com.infobip.testcontainers;

import org.springframework.context.*;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class InitializerBase<C extends Startable>
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener<ContextClosedEvent> {

    public static final String PORT_PLACEHOLDER = "<port>";
    public static final String HOST_PLACEHOLDER = "<host>";

    protected static final Pattern GENERIC_URL_WITH_PORT_GROUP_PATTERN = Pattern.compile(".*://.*:(\\d+)(/.*)?");

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

    protected String replaceHostAndPortPlaceholders(String source,
                                                    Container<?> container,
                                                    Integer originalContainerPort) {
        return source.replaceAll(HOST_PLACEHOLDER, container.getHost())
                     .replaceAll(PORT_PLACEHOLDER, container.getMappedPort(originalContainerPort).toString());
    }

    protected Optional<Integer> resolveStaticPort(String connectionString, Pattern urlPatternWithPortGroup) {
        return Optional.ofNullable(connectionString)
                       .map(urlPatternWithPortGroup::matcher)
                       .filter(Matcher::matches)
                       .map(matcher -> matcher.group(1))
                       .map(Integer::valueOf);
    }

    protected Optional<Integer> resolveStaticPort(Collection<String> connectionStrings,
                                                  Pattern urlPatternWithPortGroup) {
        return connectionStrings.stream()
                                .flatMap(connectionString -> resolveStaticPort(connectionString,
                                                                               urlPatternWithPortGroup).stream())
                                .findFirst();
    }

    protected void bindPort(Container<?> container, Integer hostPort, Integer containerPort) {
        container.setPortBindings(Collections.singletonList(hostPort + ":" + containerPort));
    }

    protected <SELF extends GenericContainer<SELF>, T extends GenericContainer<SELF>> T handleReusable(T container) {

        if (TestcontainersConfiguration.getInstance().environmentSupportsReuse()) {
            container.withReuse(true);
        }

        return container;
    }

    protected void registerContainerAsBean(ConfigurableApplicationContext applicationContext) {
        var c = container.get();
        var containerClassName = c.getClass().getSimpleName();
        var beanName = Character.toLowerCase(containerClassName.charAt(0)) + containerClassName.substring(1);
        applicationContext.getBeanFactory()
                          .registerSingleton(beanName, c);
    }
}
