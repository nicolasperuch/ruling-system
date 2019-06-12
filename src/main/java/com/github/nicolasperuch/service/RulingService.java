package com.github.nicolasperuch.service;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.entity.RulingEntity;
import org.springframework.stereotype.Service;

@Service
public class RulingService {

    public RulingEntity createRuling(RulingDto rulingDto){
        RulingEntity rulingEntity = new RulingEntity();
        rulingEntity.setId(1);
        rulingEntity.setName(rulingDto.getName());
        //should save into database here
        return rulingEntity;
    }
}
