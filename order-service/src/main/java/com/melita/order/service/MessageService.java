package com.melita.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melita.order.entities.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final RabbitTemplate amqpTemplate;

    private final Queue queue;

    public void sendMessage(Order order) {

        // publish message as async
        new Thread(() -> {
            try {
                amqpTemplate.convertAndSend(queue.getName(), order.getNumber());
            } catch (Exception ex) {
                log.error("error while publishing message to RabbitMQ for order id {}", order.getId(), ex);
            }
        }).start();
    }
}
