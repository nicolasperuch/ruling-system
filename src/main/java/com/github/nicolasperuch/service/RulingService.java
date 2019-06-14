package com.github.nicolasperuch.service;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.config.RabbitRulingConfig;
import com.github.nicolasperuch.entity.RulingEntity;
import com.github.nicolasperuch.model.OpenRulingForVoteModel;
import com.github.nicolasperuch.repository.RulingRepository;
import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RulingService extends RabbitRulingConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Gson gson;
    @Autowired
    private RulingRepository rulingRepository;

    public RulingEntity createRuling(RulingDto rulingDto){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setName(rulingDto.getName());
        rulingEntity.setOpenForVote(true);
        rulingRepository.save(rulingEntity);
        return rulingEntity;
    }

    public boolean openRulingForVote(OpenRulingForVoteModel openRulingForVoteModel) {
        //should save into dabase here
        feedExchange(openRulingForVoteModel);
        return true;
    }

    public void feedExchange(OpenRulingForVoteModel openRulingForVoteModel) {
        rabbitTemplate.convertAndSend(EXCHANGE, SESSION_STARTED_QUEUE, buildMessage(openRulingForVoteModel));
    }

    public Message buildMessage(OpenRulingForVoteModel openRulingForVoteModel) {
        String bodyMessage = gson.toJson(openRulingForVoteModel);
        return new Message(bodyMessage.getBytes(), new MessageProperties());
    }

}
