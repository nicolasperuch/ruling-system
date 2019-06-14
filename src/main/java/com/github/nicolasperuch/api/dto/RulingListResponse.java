package com.github.nicolasperuch.api.dto;

import com.github.nicolasperuch.entity.RulingEntity;

import java.util.List;

public class RulingListResponse {
    private List<RulingEntity> rulings;

    public RulingListResponse(List<RulingEntity> rulings) {
        this.rulings = rulings;
    }

    public List<RulingEntity> getRulings() {
        return rulings;
    }

    public void setRulings(List<RulingEntity> rulings) {
        this.rulings = rulings;
    }
}
