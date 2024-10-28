package com.example.blog.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.blog.dto.CategoryDTO;
import com.example.blog.dto.PostDTO;
import com.example.blog.dto.TagDTO;
import com.example.blog.dto.UserDTO;
import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;

public class PostMapper {

    public static Post toEntity(PostDTO dto, User author,
            List<Category> categories, List<Tag> tags) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setSlug(dto.getSlug());
        post.setPublished(dto.isPublished());
        post.setAuthor(author); // Set the author entity

        if (categories != null) {
            post.setCategories(categories); // Set the category entities
        }

        if (tags != null) {
            post.setTags(tags); // Set the tag entities
        }

        return post;
    }

    public static PostDTO toDTO(Post post) {
        // Konversi author ke DTO
        UserDTO authorDTO = new UserDTO(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getEmail()
        );

        // Konversi categories dan tags ke DTO
        List<CategoryDTO> categoryDTOs = post.getCategories().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());

        List<TagDTO> tagDTOs = post.getTags().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());

        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.isPublished(),
                authorDTO,
                categoryDTOs,
                tagDTOs,
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,  // Pastikan ini ada
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null
        );
    }
}
