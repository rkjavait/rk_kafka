package com.rk.kfproduce.service;

import com.rk.kfproduce.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderKfMessageService {

   private static final Logger log = LoggerFactory.getLogger(OrderKfMessageService.class);

   private String message;

   @KafkaListener(topics = "order-service", groupId = "order-consumer-group")
   public void receiveMessage(Order order) {
       long startTime = System.nanoTime();

       try {
           message = "Received order from Kafka: id=" + order.getId() + ", type=" + order.getOrderType() + ", customerName=" + order.getCustomerName();
           log.info("Processing Kafka order event | id={}, orderType={}, customerName={}", order.getId(), order.getOrderType(), order.getCustomerName());
           log.debug("Order payload processed successfully: {}", message);
       } catch (Exception e) {
           log.error("Failed to process Kafka order event: {}", order, e);
           throw e;
       } finally {
           long elapsedMs = (System.nanoTime() - startTime) / 1_000_000L;
           log.info("Kafka order processing profile | elapsedMs={} | orderId={}", elapsedMs, order != null ? order.getId() : null);
       }
   }

   public String getMessage() {
       return message;
   }
}
