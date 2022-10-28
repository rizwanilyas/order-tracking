package com.melita.notification.listener;

import com.melita.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageListener {

    private final EmailService emailService;

    @RabbitListener(queues = {"${app.rabbit.queue.name}"})
    public void receive(@Payload String orderNumber) {
        sendNotification(orderNumber);
    }

    private void sendNotification(String orderNumber) {
        String msg = String.format("A new order %s created", orderNumber);
        emailService.sendEmail(msg, msg);
    }
}
