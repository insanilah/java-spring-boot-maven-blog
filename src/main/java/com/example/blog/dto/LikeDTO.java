package com.example.blog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long id;

    @NotNull(message = "Post ID cannot be null")
    private Long postId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private String likedAt;
}
