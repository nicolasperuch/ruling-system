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
import java.util.logging.Logger;

@Service
public class RulingService extends RabbitRulingConfig {

    private RabbitTemplate rabbitTemplate;
    private Gson gson;
    private RulingRepository rulingRepository;
    static Logger log = Logger.getLogger(RulingService.class.getName());
    private final String OPEN_RULING_MESSAGE = "Ruling opened for vote succesfully";
    private final String OPEN_RULING_ERROR_MESSAGE = "It Was not possible open this ruling for vote";

    public RulingService(RabbitTemplate rabbitTemplate, Gson gson, RulingRepository rulingRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
        this.rulingRepository = rulingRepository;
    }

    public RulingEntity createRuling(RulingDto rulingDto){
        log.info("Request receive to create a ruling");
        log.info(rulingDto.toString());
        RulingEntity rulingEntity = dtoToEntity(rulingDto);
        log.info("Saving Ruling into database");
        return rulingRepository.save(rulingEntity);
    }

    public RulingForVoteResponse openRulingForVote(OpenRulingForVoteModel openRulingForVoteModel) {
        log.info("Checking if ruling exists");
        Optional<RulingEntity> rulingEntity = rulingRepository.findById(openRulingForVoteModel.getRulingId());
        if(rulingEntity.isPresent() && isRulingAbleToOpenToVote(rulingEntity.get())){
            RulingEntity entity = setRulingStatusToOpenForVote(rulingEntity.get());
            log.info("Saving ruling open for vote into database");
            rulingRepository.save(entity);
            feedExchange(openRulingForVoteModel);
            log.info("RESPONSE: " + OPEN_RULING_MESSAGE);
            return new RulingForVoteResponse(OPEN_RULING_MESSAGE);
        }
        log.info("RESPONSE: " + OPEN_RULING_ERROR_MESSAGE);
        return new RulingForVoteResponse(OPEN_RULING_ERROR_MESSAGE);
    }

    public boolean isRulingAbleToOpenToVote(RulingEntity rulingEntity){
        log.info("Checking if ruling is Able to open to vote");
        return !rulingEntity.isOpenForVote();
    }

    public RulingEntity setRulingStatusToOpenForVote(RulingEntity rulingEntity){
        log.info("Changing ruling status to open for vote");
        rulingEntity.setOpenForVote(true);
        log.info(rulingEntity.toString());
        return rulingEntity;
    }

    public void feedExchange(OpenRulingForVoteModel openRulingForVoteModel) {
        log.info("Publishing ruling open for vote event into exchange");
        rabbitTemplate.convertAndSend(EXCHANGE, SESSION_STARTED_QUEUE, buildMessage(openRulingForVoteModel));
    }

    public Message buildMessage(OpenRulingForVoteModel openRulingForVoteModel) {
        log.info("Building Rabbit Message from model");
        String bodyMessage = gson.toJson(openRulingForVoteModel);
        return new Message(bodyMessage.getBytes(), new MessageProperties());
    }

    public RulingEntity dtoToEntity(RulingDto rulingDto){
        log.info("Building entity from RulingDto");
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setName(rulingDto.getName());
        rulingEntity.setOpenForVote(false);
        log.info(rulingEntity.toString());
        return rulingEntity;
    }
}