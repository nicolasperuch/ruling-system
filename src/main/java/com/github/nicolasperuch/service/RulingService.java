package com.github.nicolasperuch.service;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.entity.RulingEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RulingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public RulingEntity createRuling(RulingDto rulingDto){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setId(1);
        rulingEntity.setName(rulingDto.getName());
        //should save into database here
        return rulingEntity;
    }

    public void feedExchange(RulingEntity rulingEntity) {
        rabbitTemplate.convertAndSend("ruling", "type", buildMessage(rulingEntity));
    }

    public Message buildMessage(RulingEntity rulingEntity) {
        String bodyMessage = rulingEntity.toString();
        return new Message(bodyMessage.getBytes(), new MessageProperties());
    }

}
