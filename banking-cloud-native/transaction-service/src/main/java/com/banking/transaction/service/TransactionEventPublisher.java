package com.banking.transaction.service;

import com.banking.transaction.config.RabbitMQConfig;
import com.banking.transaction.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishTransactionEvent(Transaction transaction) {
        String message = String.format(
                "TRANSACTION|%s|%s|%s|%s",
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTimestamp()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BANKING_EXCHANGE,
                RabbitMQConfig.TRANSACTION_ROUTING_KEY,
                message
        );
    }
}
