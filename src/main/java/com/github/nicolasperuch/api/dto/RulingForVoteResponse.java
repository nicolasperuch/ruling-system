package com.github.nicolasperuch.api.dto;

public class RulingForVoteResponse {
    private String message;

    public RulingForVoteResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
