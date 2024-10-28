package com.example.blog.service.interfaces;

import com.example.blog.dto.PostDTO;
import com.example.blog.dto.auth.RegisterDTO;

public interface NotificationService {
    void sendArticlePublishedNotification(PostDTO notification);
    void sendUserRegistrationNotification(RegisterDTO notification);
}
