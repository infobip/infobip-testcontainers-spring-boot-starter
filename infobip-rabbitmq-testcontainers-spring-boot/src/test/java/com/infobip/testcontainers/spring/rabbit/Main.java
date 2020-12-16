package com.infobip.testcontainers.spring.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import static org.springframework.amqp.core.BindingBuilder.bind;

@SpringBootApplication
public class Main {

    @Bean
    public Queue testQueue() {
        return QueueBuilder.durable("test.queue").build();
    }

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange("test.exchange");
    }

    @Bean
    public Binding bindToTestExchange() {
        return bind(testQueue()).to(testExchange()).with("test.key.#");
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class).run(args);
    }
}