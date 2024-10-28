package com.example.blog.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    // Exchange dan queue untuk artikel
    public static final String ARTICLE_EXCHANGE = "article_exchange";
    public static final String ARTICLE_QUEUE = "article_notification_queue";
    public static final String ARTICLE_ROUTING_KEY = "article.published";

    // Exchange dan queue untuk registrasi pengguna
    public static final String USER_EXCHANGE = "user_exchange";
    public static final String USER_QUEUE = "user_registration_queue";
    public static final String USER_ROUTING_KEY = "user.registered";

    // Exchange dan queue untuk artikel
    @Bean
    public DirectExchange articleExchange() {
        return new DirectExchange(ARTICLE_EXCHANGE, true, false); // Durable exchange
    }

    @Bean
    public Queue articleNotificationQueue() {
        return new Queue(ARTICLE_QUEUE, true); // Durable queue
    }

    @Bean
    public Binding articleBinding(DirectExchange articleExchange, Queue articleNotificationQueue) {
        return BindingBuilder.bind(articleNotificationQueue)
                .to(articleExchange)
                .with(ARTICLE_ROUTING_KEY);
    }

    // Exchange dan queue untuk registrasi pengguna
    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE, true, false); // Durable exchange
    }

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(USER_QUEUE, true); // Durable queue
    }

    @Bean
    public Binding userBinding(DirectExchange userExchange, Queue userRegistrationQueue) {
        return BindingBuilder.bind(userRegistrationQueue)
                .to(userExchange)
                .with(USER_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true); // Enable publisher confirms
        return rabbitTemplate;
    }
}

