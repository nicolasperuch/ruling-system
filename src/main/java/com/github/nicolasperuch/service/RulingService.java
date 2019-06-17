package com.github.nicolasperuch.service;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.api.dto.RulingForVoteResponse;
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

import java.util.Optional;

@Service
public class RulingService extends RabbitRulingConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Gson gson;
    @Autowired
    private RulingRepository rulingRepository;
    private final String OPEN_RULING_MESSAGE = "Ruling opened for vote succesfully";
    private final String OPEN_RULING_ERROR_MESSAGE = "Was not possible open this ruling for vote";

    public RulingEntity createRuling(RulingDto rulingDto){
        RulingEntity rulingEntity = dtoToEntity(rulingDto);
        rulingRepository.save(rulingEntity);
        return rulingEntity;
    }

    public RulingForVoteResponse openRulingForVote(OpenRulingForVoteModel openRulingForVoteModel) {
        Optional<RulingEntity> rulingEntity = rulingRepository.findById(openRulingForVoteModel.getRulingId());
        if(isExistentRuling(rulingEntity)){
            RulingEntity entity = setRulingStatusToOpenForVote(rulingEntity);
            rulingRepository.save(entity);
            feedExchange(openRulingForVoteModel);
            return new RulingForVoteResponse(OPEN_RULING_MESSAGE);
        }
        return new RulingForVoteResponse(OPEN_RULING_ERROR_MESSAGE);
    }

    public boolean isExistentRuling(Optional<RulingEntity> rulingEntity){
        return rulingEntity.isPresent();
    }

    public RulingEntity setRulingStatusToOpenForVote(Optional<RulingEntity> rulingEntity){
        RulingEntity entity = rulingEntity.get();
        entity.setOpenForVote(true);
        return entity;
    }

    public void feedExchange(OpenRulingForVoteModel openRulingForVoteModel) {
        rabbitTemplate.convertAndSend(EXCHANGE, SESSION_STARTED_QUEUE, buildMessage(openRulingForVoteModel));
    }

    public Message buildMessage(OpenRulingForVoteModel openRulingForVoteModel) {
        String bodyMessage = gson.toJson(openRulingForVoteModel);
        return new Message(bodyMessage.getBytes(), new MessageProperties());
    }

    public RulingEntity dtoToEntity(RulingDto rulingDto){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setName(rulingDto.getName());
        rulingEntity.setOpenForVote(false);
        return rulingEntity;
    }
}