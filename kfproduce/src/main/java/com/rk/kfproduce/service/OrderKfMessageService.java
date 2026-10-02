package com.rk.kfproduce.service;

import com.rk.kfproduce.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKfMessageService {

    private static final Logger log = LoggerFactory.getLogger(OrderKfMessageService.class);

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderKfMessageService(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendMessage(Order order) {
        long startTime = System.nanoTime();
        log.info("Publishing order event for orderId={} to topic='order-service'", order.getId());

        kafkaTemplate.send("order-service", String.valueOf(order.getId()), order);

        long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;
        log.info("Order event published successfully for orderId={} in {} ms", order.getId(), elapsedMs);

        return "Order message sent to Kafka topic 'order-service' for orderId " + order.getId();
    }
}
