package org.customerapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String CUSTOMERS_QUEUE = "customers.queue";
    public static final String EXCHANGE = "flowers-exchange-events";
    public static final String ROUTING_KEY = "events.flower.routing.key";

    public static final String GENERAL_QUEUE = "general.queue";
    public static final String ROUTING_KEY_GENERAL = "events.#";

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        var admin =  new RabbitAdmin(rabbitTemplate);
        admin.declareExchange(new TopicExchange(EXCHANGE));
        return admin;
    }

    //Set bean to queue
    @Bean
    public Queue eventsQueue(){
        return new Queue(CUSTOMERS_QUEUE);
    }

    @Bean
    public Queue eventsGeneralQueue(){
        return new Queue(GENERAL_QUEUE);
    }

    //Set bean to exchange
    @Bean
    public TopicExchange eventsExchange(){
        return new TopicExchange(EXCHANGE);
    }

    //Set beans related to binding
    @Bean
    public Binding eventsBinding(){
        return BindingBuilder.bind(this.eventsQueue()).to(this.eventsExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding eventsGeneralBinding(){
        return BindingBuilder.bind(this.eventsGeneralQueue()).to(this.eventsExchange()).with(ROUTING_KEY_GENERAL);
    }
}
