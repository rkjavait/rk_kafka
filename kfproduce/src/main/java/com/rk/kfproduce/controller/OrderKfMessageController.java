package com.rk.kfproduce.controller;

import com.rk.kfproduce.model.Order;
import com.rk.kfproduce.service.OrderKfMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OrderKfMessageController {

    private static final Logger log = LoggerFactory.getLogger(OrderKfMessageController.class);

    private final OrderKfMessageService kfMessageService;

    public OrderKfMessageController(OrderKfMessageService kfMessageService) {
        this.kfMessageService = kfMessageService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        log.info("Incoming order request received for orderId={}", order.getId());
        String responseMessage = kfMessageService.sendMessage(order);
        log.info("Order request completed for orderId={}. Response={}", order.getId(), responseMessage);
        return new ResponseEntity<>(responseMessage, HttpStatus.ACCEPTED);
    }
}
