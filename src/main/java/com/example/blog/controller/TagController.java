package com.example.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.Response;
import com.example.blog.dto.TagDTO;
import com.example.blog.model.Tag;
import com.example.blog.service.TagServiceImpl;
import com.example.blog.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagServiceImpl tagService;

     // Endpoint untuk mendapatkan semua post dengan pagination sederhana
     @GetMapping
     public ResponseEntity<Response<Map<String, Object>>> getAllTagsCustomQuery(
         @RequestParam(required = false) String name,
         @RequestParam(defaultValue = "1") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

         // Parsing sorting input
         Sort.Direction direction = Sort.Direction.fromString(sort[1]);
         String sortBy = sort[0];

         Page<Tag> postPage;
         if (name == null || name.isEmpty()) {
            log.info("name:{}",name);
            postPage = tagService.getAllTags(PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
         } else {
            postPage = tagService.getAllTagByName(name, PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
         }

         // Membuat response
         Map<String, Object> data = new HashMap<>();
         data.put("tags", postPage.getContent());
         data.put("totalPages", postPage.getTotalPages());
         data.put("totalElements", postPage.getTotalElements());
         data.put("currentPage", postPage.getNumber() + 1);
         data.put("pageSize", postPage.getSize());
     
         return ResponseBuilder.buildResponse("200", "Success", data, HttpStatus.OK);
     }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Tag>> getTagById(@PathVariable Long id) {
        Tag post = tagService.getTagById(id);
        return ResponseBuilder.buildResponse("200", "Success", post, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<Tag>> createTag(@Valid @RequestBody TagDTO postDTO) {
        Tag createdTag = tagService.createTag(postDTO);
        return ResponseBuilder.buildResponse("201", "Success Created", createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Tag>> updateTag(@Valid @PathVariable Long id, @RequestBody TagDTO postDTO) {
        Tag updatedTag = tagService.updateTag(id, postDTO);
        return ResponseBuilder.buildResponse("200", "Success Updated", updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<TagDTO>> deleteTag(@PathVariable Long id) {
        TagDTO deletedTag = tagService.deleteTag(id);
        return ResponseBuilder.buildResponse("200", "Success Deleted", deletedTag, HttpStatus.OK);
    }
}