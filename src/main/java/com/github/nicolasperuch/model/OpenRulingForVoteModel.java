package com.github.nicolasperuch.model;

public class OpenRulingForVoteModel {
    private Long rulingId;
    private String remainingTime;

    public Long getRulingId() {
        return rulingId;
    }

    public OpenRulingForVoteModel setRulingId(Long rulingId) {
        this.rulingId = rulingId;
        return this;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public OpenRulingForVoteModel setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
        return this;
    }
}
