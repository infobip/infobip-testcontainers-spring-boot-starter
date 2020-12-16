package com.infobip.testcontainers;

import org.springframework.context.*;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.lifecycle.Startable;

import java.util.concurrent.atomic.AtomicReference;

public abstract class InitializerBase<C extends Startable>
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener<ContextClosedEvent> {

    private final AtomicReference<C> container = new AtomicReference<>();

    protected void start(C container) {
        this.container.set(container);
        container.start();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        C value = container.get();
        if(value != null) {
            value.stop();
        }
    }
}
