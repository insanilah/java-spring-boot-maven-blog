package com.example.blog.dto;

import java.io.Serializable;
import java.util.List;

import com.example.blog.validation.ValidTitle;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @NotNull(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    @ValidTitle
    private String title;

    @NotNull(message = "Content cannot be empty")
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    private String slug;

    private boolean published;
    private UserDTO author;
    // Author info (usually id is enough, but you can include more details if needed)
    @NotNull(message = "Author cannot be null")
    private Long authorId;

    // List of category ids
    private List<Long> categoryIds;
    private List<CategoryDTO> categories;

    // List of tag ids
    private List<Long> tagIds;
    private List<TagDTO> tags;

    // Auto-generated fields (readonly in DTO)
    private String createdAt;

    private String updatedAt;

    public boolean isPublished() {
        return published;
    }

     // Constructor khusus yang dibutuhkan
     public PostDTO(Long id, String title, String content, String slug, boolean published,
            UserDTO author, List<CategoryDTO> categories, List<TagDTO> tags, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.slug = slug;
        this.published = published;
        this.author = author;
        this.categories = categories;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PostDTO(String title, String content, String slug, boolean published,
            UserDTO author, List<CategoryDTO> categories, List<TagDTO> tags, String createdAt, String updatedAt) {
        this.title = title;
        this.content = content;
        this.slug = slug;
        this.published = published;
        this.author = author;
        this.categories = categories;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PostDTO(String title, String content, String slug, boolean published,
            Long authorId, List<Long> categoryIds, List<CategoryDTO> categories, List<Long> tagIds, List<TagDTO> tags, String createdAt, String updatedAt) {
        this.title = title;
        this.content = content;
        this.slug = slug;
        this.published = published;
        this.authorId = authorId;
        this.categoryIds = categoryIds;
        this.categories = categories;
        this.tagIds = tagIds;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
