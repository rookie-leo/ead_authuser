package com.ead.authuser.publishers;

import com.ead.authuser.controllers.dtos.UserEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Value(value = "ead.userevent")
    private String exchangeUserEvent;

    final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserEvent(UserEventDto userEventDto) {
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEventDto);
    }
}
