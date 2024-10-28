package com.example.blog.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.blog.configs.RabbitConfig;
import com.example.blog.dto.PostDTO;
import com.example.blog.dto.auth.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NotificationListener {

    private final ObjectMapper objectMapper;

    public NotificationListener() {
        this.objectMapper = new ObjectMapper(); // Inisialisasi ObjectMapper
    }

    // Mendengarkan notifikasi untuk artikel
    @RabbitListener(queues = RabbitConfig.ARTICLE_QUEUE)
    public void receiveArticleNotification(String message) {
        try {
            PostDTO notification = objectMapper.readValue(message, PostDTO.class); // Mengubah JSON menjadi objek
            System.out.println("Notifikasi Artikel diterima: Title (" + notification.getTitle() + ") - Author (" + notification.getAuthorId()+")");
            // Logika pengiriman email untuk artikel
        } catch (Exception e) {
            // Tangani exception sesuai kebutuhan
            e.printStackTrace();
        }
    }

    // Mendengarkan notifikasi untuk registrasi pengguna
    @RabbitListener(queues = RabbitConfig.USER_QUEUE)
    public void receiveUserRegistrationNotification(String message) {
        try {
            RegisterDTO notification = objectMapper.readValue(message, RegisterDTO.class); // Mengubah JSON menjadi objek
            System.out.println("Notifikasi Registrasi Pengguna diterima: Username (" + notification.getUsername() + ") - Email (" + notification.getEmail()+")");
            // Logika pengiriman email untuk registrasi
        } catch (Exception e) {
            // Tangani exception sesuai kebutuhan
            e.printStackTrace();
        }
    }
}
