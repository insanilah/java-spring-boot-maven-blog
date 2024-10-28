package com.example.blog.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityDTO {
    private String id;
    private String username;
    private String activityType;
    private Long postId;
    private String timestamp; // Formatted as String for API response
}
