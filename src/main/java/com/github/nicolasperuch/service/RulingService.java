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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RulingService extends RabbitRulingConfig {

    private RabbitTemplate rabbitTemplate;
    private Gson gson;
    private RulingRepository rulingRepository;

    public RulingService(RabbitTemplate rabbitTemplate, Gson gson, RulingRepository rulingRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
        this.rulingRepository = rulingRepository;
    }

    private final String OPEN_RULING_MESSAGE = "Ruling opened for vote succesfully";
    private final String OPEN_RULING_ERROR_MESSAGE = "Was not possible open this ruling for vote";

    public RulingEntity createRuling(RulingDto rulingDto){
        RulingEntity rulingEntity = dtoToEntity(rulingDto);
        return rulingRepository.save(rulingEntity);
    }

    public RulingForVoteResponse openRulingForVote(OpenRulingForVoteModel openRulingForVoteModel) {
        Optional<RulingEntity> rulingEntity = rulingRepository.findById(openRulingForVoteModel.getRulingId());
        if(rulingEntity.isPresent()){
            RulingEntity entity = setRulingStatusToOpenForVote(rulingEntity.get());
            rulingRepository.save(entity);
            feedExchange(openRulingForVoteModel);
            return new RulingForVoteResponse(OPEN_RULING_MESSAGE);
        }
        return new RulingForVoteResponse(OPEN_RULING_ERROR_MESSAGE);
    }

    public RulingEntity setRulingStatusToOpenForVote(RulingEntity rulingEntity){
        rulingEntity.setOpenForVote(true);
        return rulingEntity;
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