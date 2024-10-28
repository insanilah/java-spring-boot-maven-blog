package com.example.blog.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.blog.configs.RabbitConfig;
import com.example.blog.dto.PostDTO;
import com.example.blog.dto.auth.RegisterDTO;
import com.example.blog.service.interfaces.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper

    public NotificationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper(); // Inisialisasi ObjectMapper
    }

    // Mengirim notifikasi saat artikel dipublikasikan
    @Override
    public void sendArticlePublishedNotification(PostDTO notification) {
        try {
            String message = objectMapper.writeValueAsString(notification); // Mengubah objek menjadi JSON
            rabbitTemplate.convertAndSend(RabbitConfig.ARTICLE_EXCHANGE, RabbitConfig.ARTICLE_ROUTING_KEY, message);
        } catch (Exception e) {
            // Tangani exception sesuai kebutuhan
            e.printStackTrace();
        }
    }

    // Mengirim notifikasi saat pengguna berhasil registrasi
    @Override
    public void sendUserRegistrationNotification(RegisterDTO notification) {
        try {
            String message = objectMapper.writeValueAsString(notification); // Mengubah objek menjadi JSON
            rabbitTemplate.convertAndSend(RabbitConfig.USER_EXCHANGE, RabbitConfig.USER_ROUTING_KEY, message);
        } catch (Exception e) {
            // Tangani exception sesuai kebutuhan
            e.printStackTrace();
        }
    }
}
