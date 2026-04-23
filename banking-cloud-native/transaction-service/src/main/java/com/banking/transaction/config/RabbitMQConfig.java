package com.banking.transaction.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TRANSACTION_EVENTS_QUEUE = "transaction.events";
    public static final String BANKING_EXCHANGE = "banking.exchange";
    public static final String TRANSACTION_ROUTING_KEY = "transaction.key";

    @Bean
    public Queue transactionEventsQueue() {
        return new Queue(TRANSACTION_EVENTS_QUEUE);
    }

    @Bean
    public DirectExchange bankingExchange() {
        return new DirectExchange(BANKING_EXCHANGE);
    }

    @Bean
    public Binding transactionEventsBinding(Queue transactionEventsQueue, DirectExchange bankingExchange) {
        return BindingBuilder.bind(transactionEventsQueue).to(bankingExchange).with(TRANSACTION_ROUTING_KEY);
    }
}
