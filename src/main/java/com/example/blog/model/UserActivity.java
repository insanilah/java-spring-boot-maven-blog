package com.example.blog.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_activities")
public class UserActivity {

    @Id
    private String id;
    private String username;
    private String activityType; // e.g., "view", "like", etc.
    private Long postId;
    private LocalDateTime timestamp;

    // Constructor that accepts a String for timestamp
    public UserActivity(String id, String username, String activityType, Long postId, String timestamp) {
        this.id = id;
        this.username = username;
        this.activityType = activityType;
        this.postId = postId;
        // Use the Indonesian formatter to parse the timestamp string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        this.timestamp = LocalDateTime.parse(timestamp, formatter); // Convert String to LocalDateTime
    }
}
