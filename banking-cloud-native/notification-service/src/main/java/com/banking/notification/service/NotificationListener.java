package com.banking.notification.service;

import com.banking.notification.model.Notification;
import com.banking.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class NotificationListener {

    private static final Logger LOGGER = Logger.getLogger(NotificationListener.class.getName());

    private final NotificationRepository notificationRepository;

    public NotificationListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "transaction.events")
    public void onTransactionEvent(String message) {
        String[] parts = message.split("\\|");
        if (parts.length < 5 || !"TRANSACTION".equals(parts[0])) {
            LOGGER.warning("Invalid transaction event received: " + message);
            return;
        }

        Long accountId = Long.parseLong(parts[1]);
        String type = parts[2];
        String amount = parts[3];

        LOGGER.info("NOTIFICATION SENT for account " + accountId + ": " + type + " of " + amount);
        Notification notification = new Notification(accountId, type, amount, LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
