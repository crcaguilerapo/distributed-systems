package com.crcaguilerapo.serviceB.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerB {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.end-point.url}")
    private String endpoint;

    public ControllerB(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @SqsListener(value = "${cloud.aws.end-point.name}")
    public void handlerEvent(String message) {
        try {
            System.out.println(message);
        } catch (Exception e) {
            rollbackEvent("rollback");
        }
    }

    public void rollbackEvent(@PathVariable String message) {
        queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());
    }
}