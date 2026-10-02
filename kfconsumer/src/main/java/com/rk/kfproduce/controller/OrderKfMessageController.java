package com.rk.kfproduce.controller;

import com.rk.kfproduce.service.OrderKfMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OrderKfMessageController {

    @Autowired
    private OrderKfMessageService kfMessageService;

    @GetMapping("/order/message")
    public ResponseEntity<String> getMessage(){

        //logic to read message to consumer
        String message = kfMessageService.getMessage();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
