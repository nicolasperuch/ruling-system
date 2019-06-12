package com.github.nicolasperuch.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig extends RabbitRulingConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(HOST, PORT);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        return connectionFactory;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue sessionStartedQueue(){
        return new Queue(SESSION_STARTED_QUEUE, true, false, false);
    }

    @Bean
    public Queue sessionEndedQueue(){
        return new Queue(SESSION_ENDED_QUEUE, true, false, false);
    }

    @Bean
    public Binding bindingExchangeToSessionStartedQueue(){
        return BindingBuilder
                .bind(sessionStartedQueue())
                .to(topicExchange())
                .with(SESSION_STARTED_QUEUE);
    }

    @Bean
    public Binding bindingExchangeToSessionEndedQueue(){
        return BindingBuilder
                .bind(sessionEndedQueue())
                .to(topicExchange())
                .with(SESSION_ENDED_QUEUE);
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(topicExchange());
        rabbitAdmin.declareQueue(sessionStartedQueue());
        rabbitAdmin.declareQueue(sessionEndedQueue());
        rabbitAdmin.declareBinding(bindingExchangeToSessionStartedQueue());
        rabbitAdmin.declareBinding(bindingExchangeToSessionEndedQueue());
        return rabbitAdmin;
    }
}
