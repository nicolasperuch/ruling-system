package com.github.nicolasperuch.api;

import com.github.nicolasperuch.api.dto.OpenRulingForVoteDto;
import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.api.dto.RulingListResponse;
import com.github.nicolasperuch.api.dto.RulingResponse;
import com.github.nicolasperuch.api.exception.handler.ExceptionHandlerApi;
import com.github.nicolasperuch.entity.RulingEntity;
import com.github.nicolasperuch.model.OpenRulingForVoteModel;
import com.github.nicolasperuch.repository.RulingRepository;
import com.github.nicolasperuch.service.RulingService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/ruling")
public class RulingApi extends ExceptionHandlerApi {

    @Autowired
    private RulingService rulingService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RulingRepository rulingRepository;

    @ApiOperation(value = "Create a ruling")
    @PostMapping
    public ResponseEntity<?> createRuling(@Valid @RequestBody RulingDto rulingDto) {
        return Stream
                .of(rulingDto)
                .map(dto -> rulingService.createRuling(dto))
                .map(this::entityToResponseDto)
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }

    @ApiOperation(value = "Open a ruling for votation")
    @PostMapping(value = "{id}/openForVote")
    public ResponseEntity<?> openRulingForVote(@PathVariable("id") Integer id,
                                               @RequestBody OpenRulingForVoteDto
                                                            openRulingForVoteDto) {
        return Stream
            .of(buildOpenRulingForVoteModel(id, openRulingForVoteDto))
            .map(model -> rulingService.openRulingForVote(model))
            .map(ResponseEntity::ok)
            .findFirst()
            .get();
    }

    @ApiOperation(value = "Get All rulings")
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return Stream
                .of(rulingRepository.findAll())
                .map(this::entityToRulingListResponseDto)
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }

    private RulingListResponse entityToRulingListResponseDto(List<RulingEntity> rulingEntityList){
        return new RulingListResponse(rulingEntityList);
    }

    private RulingResponse entityToResponseDto(RulingEntity rulingEntity){
        return modelMapper.map(rulingEntity, RulingResponse.class);
    }

    private OpenRulingForVoteModel buildOpenRulingForVoteModel(Integer id, OpenRulingForVoteDto dto){
        return new OpenRulingForVoteModel()
                    .setRulingId(id)
                    .setRemainingTime(dto.getRemainingTime());
    }
}
