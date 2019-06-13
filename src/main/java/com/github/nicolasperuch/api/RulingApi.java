package com.github.nicolasperuch.api;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.api.dto.RulingResponse;
import com.github.nicolasperuch.api.exception.handler.ExceptionHandlerApi;
import com.github.nicolasperuch.entity.RulingEntity;
import com.github.nicolasperuch.service.RulingService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping("/ruling")
public class RulingApi extends ExceptionHandlerApi {

    @Autowired
    private RulingService rulingService;
    @Autowired
    private ModelMapper modelMapper;

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

    private RulingResponse entityToResponseDto(RulingEntity rulingEntity){
        return modelMapper.map(rulingEntity, RulingResponse.class);
    }
}
