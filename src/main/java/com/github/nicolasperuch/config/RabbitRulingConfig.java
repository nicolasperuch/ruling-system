package com.github.nicolasperuch.config;

public abstract class RabbitRulingConfig {
    protected final String HOST = "localhost";
    protected final Integer PORT = 5672;
    protected final String USERNAME = "guest";
    protected final String PASSWORD = "guest";
    protected final String EXCHANGE = "ruling";
    protected final String SESSION_STARTED_QUEUE = "session-started-queue";
    protected final String SESSION_ENDED_QUEUE = "session-ended-queue";
}
