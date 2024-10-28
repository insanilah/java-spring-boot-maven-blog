package com.example.blog.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    @NotNull(message = "Post ID cannot be null")
    private Long postId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private String createdAt;
    private String updatedAt;
}
