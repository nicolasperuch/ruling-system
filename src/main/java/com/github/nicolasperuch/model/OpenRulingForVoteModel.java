package com.github.nicolasperuch.model;

public class OpenRulingForVoteModel {
    private Integer rulingId;
    private String remainingTime;

    public Integer getRulingId() {
        return rulingId;
    }

    public OpenRulingForVoteModel setRulingId(Integer rulingId) {
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
