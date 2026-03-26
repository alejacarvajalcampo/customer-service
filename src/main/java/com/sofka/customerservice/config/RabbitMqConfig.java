package com.sofka.customerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";
    public static final String CUSTOMER_QUEUE = "customer.account.queue";
    public static final String CUSTOMER_ROUTING_KEY = "customer.sync";
    public static final String CUSTOMER_DLX = "customer.exchange.dlx";
    public static final String CUSTOMER_DLQ = "customer.account.queue.dlq";
    public static final String CUSTOMER_DLQ_ROUTING_KEY = "customer.sync.dlq";

    @Bean
    public DirectExchange customerExchange() {
        return new DirectExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public DirectExchange customerDeadLetterExchange() {
        return new DirectExchange(CUSTOMER_DLX);
    }

    @Bean
    public Queue customerQueue() {
        return QueueBuilder.durable(CUSTOMER_QUEUE)
                .deadLetterExchange(CUSTOMER_DLX)
                .deadLetterRoutingKey(CUSTOMER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue customerDeadLetterQueue() {
        return QueueBuilder.durable(CUSTOMER_DLQ).build();
    }

    @Bean
    public Binding customerBinding(Queue customerQueue, DirectExchange customerExchange) {
        return BindingBuilder.bind(customerQueue).to(customerExchange).with(CUSTOMER_ROUTING_KEY);
    }

    @Bean
    public Binding customerDeadLetterBinding(Queue customerDeadLetterQueue, DirectExchange customerDeadLetterExchange) {
        return BindingBuilder.bind(customerDeadLetterQueue).to(customerDeadLetterExchange).with(CUSTOMER_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
