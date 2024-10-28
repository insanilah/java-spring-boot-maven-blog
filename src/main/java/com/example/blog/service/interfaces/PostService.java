package com.example.blog.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.blog.dto.PostDTO;

public interface PostService {
    List<PostDTO> getAllPosts();
    Page<PostDTO> getAllPosts(Pageable pageable);
    Map<String, Object> getAllPostsCustomPagination(int page, int size, String[] sort);
    Map<String, Object> getAllPostsCustomQuery(String title, int page, int size, String[] sort);
    PostDTO getPostById(String username, Long id);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long id, PostDTO postDTO);
    PostDTO deletePost(Long id);
    PostDTO getPostFromRedis(Long id);
    PostDTO setPostFromRedis(PostDTO postDTO);
}