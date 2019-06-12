package com.github.nicolasperuch.api;

import com.github.nicolasperuch.api.dto.RulingDto;
import com.github.nicolasperuch.service.RulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/ruling")
public class RulingApi {

    @Autowired
    private RulingService rulingService;

    @PostMapping
    public ResponseEntity<?> createRuling(@RequestBody RulingDto rulingDto) {
        return ok(rulingService.createRuling(rulingDto));
    }
}
