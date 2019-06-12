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
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost", 5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("ruling", true, false);
    }

    @Bean
    public Queue rulingQueue(){
        return new Queue("ruling-queue", true, false, false);
    }

    @Bean
    public Queue voteQueue(){
        return new Queue("vote-queue", true, false, false);
    }

    @Bean
    public Binding bindingExchangeToRulingQueue(){
        return BindingBuilder
                .bind(rulingQueue())
                .to(topicExchange())
                .with("ruling-queue");
    }

    @Bean
    public Binding bindingExchangeToVoteQueue(){
        return BindingBuilder
                .bind(voteQueue())
                .to(topicExchange())
                .with("vote-queue");
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(topicExchange());
        rabbitAdmin.declareQueue(rulingQueue());
        rabbitAdmin.declareQueue(voteQueue());
        rabbitAdmin.declareBinding(bindingExchangeToRulingQueue());
        rabbitAdmin.declareBinding(bindingExchangeToVoteQueue());
        return rabbitAdmin;
    }
}
