package com.github.nicolasperuch.service;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.api.dto.RulingForVoteResponse;
import com.github.nicolasperuch.entity.RulingEntity;
import com.github.nicolasperuch.model.OpenRulingForVoteModel;
import com.github.nicolasperuch.repository.RulingRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RulingServiceTest {

    @InjectMocks
    private RulingService rulingService;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private RulingRepository rulingRepository;
    @Mock
    private Gson gson;
    private final Long EXPECTED_BODY_BYTE_SIZE = 53L;
    private final String EXPECTED_VOTE_RESPONSE_SUCCESS_MESSAGE = "Ruling opened for vote succesfully";
    private final String EXPECTED_VOTE_RESPONSE_ERROR_MESSAGE = "Was not possible open this ruling for vote";

    @Test
    public void setRulingStatusToOpenForVoteWhenStatusIsFalseThenShouldReturnTrue(){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setOpenForVote(false);
        RulingEntity targetEntity = rulingService.setRulingStatusToOpenForVote(rulingEntity);
        assertTrue(targetEntity.isOpenForVote());
    }

    @Test
    public void buildMessageWhenModelIsNotNullThenMessageBodyByteSizeShouldBeEqualsToExpectedBodyByteSize(){
        OpenRulingForVoteModel openRulingForVoteModel = new OpenRulingForVoteModel()
                                                                .setRulingId(1)
                                                                .setRemainingTime("5");
        when(gson.toJson(ArgumentMatchers.any(OpenRulingForVoteModel.class)))
                .thenReturn(gsonMockResponse());
        Message rulingMessage = rulingService.buildMessage(openRulingForVoteModel);
        Integer byteBodySize = rulingMessage.getBody().length;
        assertEquals(EXPECTED_BODY_BYTE_SIZE, Long.valueOf(byteBodySize));
    }

    @Test
    public void creatingRulingThenShouldReturnRulingEntityWithOpenForVoteStatusFalseAndIdEqualsToOne(){
        RulingDto rulingDto = buildRulingDtoRequest();
        RulingEntity expectedRulingEntity = buildRulingEntityMockResponse();

        when(rulingRepository.save(ArgumentMatchers.any(RulingEntity.class)))
                .thenReturn(expectedRulingEntity);
        RulingEntity rulingEntityResponse = rulingService.createRuling(rulingDto);

        assertEquals(expectedRulingEntity.getId(), rulingEntityResponse.getId());
        assertEquals(expectedRulingEntity.getName(), rulingEntityResponse.getName());
        assertEquals(expectedRulingEntity.isOpenForVote(), rulingEntityResponse.isOpenForVote());
    }

    @Test
    public void openRulingForVoteWhenRulingIdExistsThenShouldReturnSuccessMessage(){
        Optional<RulingEntity> rulingEntityOptional = Optional.of(new RulingEntity());

        when(rulingRepository.findById(anyInt()))
                .thenReturn(rulingEntityOptional);
        when(rulingRepository.save(any(RulingEntity.class)))
                .thenReturn(new RulingEntity());
        when(gson.toJson(ArgumentMatchers.any(OpenRulingForVoteModel.class)))
                .thenReturn(gsonMockResponse());
        doNothing()
            .when(rabbitTemplate)
            .convertAndSend(anyString(), anyString(), any(Message.class));
        OpenRulingForVoteModel openRulingForVoteModel = new OpenRulingForVoteModel()
                                                                .setRulingId(1)
                                                                .setRemainingTime("5");
        RulingForVoteResponse voteResponse = rulingService.openRulingForVote(openRulingForVoteModel);
        assertEquals(EXPECTED_VOTE_RESPONSE_SUCCESS_MESSAGE, voteResponse.getMessage());
    }

    @Test
    public void openRulingForVoteWhenRulingIdDoesNotExistsThenShouldReturnInvalidMessage(){
        Optional<RulingEntity> rulingEntityOptional = Optional.empty();

        when(rulingRepository.findById(anyInt()))
                .thenReturn(rulingEntityOptional);
        OpenRulingForVoteModel openRulingForVoteModel = new OpenRulingForVoteModel()
                .setRulingId(1)
                .setRemainingTime("5");
        RulingForVoteResponse voteResponse = rulingService.openRulingForVote(openRulingForVoteModel);
        assertEquals(EXPECTED_VOTE_RESPONSE_ERROR_MESSAGE, voteResponse.getMessage());
    }


    public RulingDto buildRulingDtoRequest(){
        RulingDto rulingDto = new RulingDto();
        rulingDto.setName("Ruling test name");
        return rulingDto;
    }

    public RulingEntity buildRulingEntityMockResponse(){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setOpenForVote(false);
        rulingEntity.setName("Ruling test name");
        rulingEntity.setId(1);
        return rulingEntity;
    }

    public String gsonMockResponse(){
        return "{\n" +
                "    \"rulingId\" : 1,\n" +
                "    \"remainingTime\" : \"5\"    \n" +
                "}";
    }
}
