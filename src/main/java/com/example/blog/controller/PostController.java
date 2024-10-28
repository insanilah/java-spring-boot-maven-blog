package com.example.blog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.PostDTO;
import com.example.blog.dto.Response;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.PostServiceImpl;
import com.example.blog.service.interfaces.NotificationService;
import com.example.blog.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostServiceImpl postService;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;

    public PostController(PostServiceImpl postService, JwtUtil jwtUtil, NotificationService notificationService) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Response<List<PostDTO>>> getAllPosts() {
        List<PostDTO> listPost = postService.getAllPosts();
        return ResponseBuilder.buildResponse("200", "Success", listPost, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan semua post dengan pagination
    @GetMapping("/v2")
    public ResponseEntity<Response<Page<PostDTO>>> getAllPosts(Pageable pageable) {
        Page<PostDTO> listPost = postService.getAllPosts(pageable);
        return ResponseBuilder.buildResponse("200", "Success", listPost, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan semua post dengan pagination sederhana
    @GetMapping("/v3")
    public ResponseEntity<Response<Map<String, Object>>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Map<String, Object> postPage = postService.getAllPostsCustomPagination(page, size, sort);
        return ResponseBuilder.buildResponse("200", "Success", postPage, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan semua post dengan pagination sederhana
    @GetMapping("/search")
    public ResponseEntity<Response<Map<String, Object>>> getAllPostsCustomQuery(
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        // Buat PageRequest dengan Sort
        Map<String,Object> postPage = postService.getAllPostsCustomQuery(title, page, size, sort);

        return ResponseBuilder.buildResponse("200", "Success", postPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PostDTO>> getPostById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Hapus "Bearer " dan ambil token saja
        }

        String username = jwtUtil.extractUsername(token);
        PostDTO post = postService.getPostById(username, id);

        return ResponseBuilder.buildResponse("200", "Success", post, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<PostDTO>> createPost(@Valid @RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.createPost(postDTO);
        notificationService.sendArticlePublishedNotification(postDTO);
        return ResponseBuilder.buildResponse("201", "Success Created", createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<PostDTO>> updatePost(@Valid @PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(id, postDTO);
        return ResponseBuilder.buildResponse("200", "Success Updated", updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<PostDTO>> deletePost(@PathVariable Long id) {
        PostDTO deletedPost = postService.deletePost(id);
        return ResponseBuilder.buildResponse("200", "Success Deleted", deletedPost, HttpStatus.OK);
    }

    @GetMapping("/redis/{id}")
    public ResponseEntity<Response<PostDTO>> getPostFromRedis(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostFromRedis(id);
        return ResponseBuilder.buildResponse("200", "Success", postDTO, HttpStatus.OK);
    }
    
    @PostMapping("/redis")
    public ResponseEntity<Response<PostDTO>> setPostFromRedis(@RequestBody PostDTO postDTO) {
        PostDTO respPostDTO = postService.setPostFromRedis(postDTO);
        return ResponseBuilder.buildResponse("200", "Success", respPostDTO, HttpStatus.OK);
    }
}
