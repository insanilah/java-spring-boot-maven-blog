package com.example.blog.controller;

import com.example.blog.dto.CategoryDTO;
import com.example.blog.dto.Response;
import com.example.blog.service.CategoryServiceImpl;
import com.example.blog.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

     // Endpoint untuk mendapatkan semua post dengan pagination sederhana
     @GetMapping
     public ResponseEntity<Response<Map<String, Object>>> getAllCategorysCustomQuery(
             @RequestParam(required = false) String name, // Opsional, bisa null
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

         // Jika name null atau kosong, ambil semua kategori tanpa filter
         Map<String,Object> data;
         if (name == null || name.isEmpty()) {
             data = categoryService.getAllCategories(page, size, sort);
         } else {
             data = categoryService.getAllCategoriesByName(name, page, size, sort);
         }
         
         return ResponseBuilder.buildResponse("200", "Success", data, HttpStatus.OK);
     }

    @GetMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseBuilder.buildResponse("200", "Success", category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO postDTO) {
        CategoryDTO category = categoryService.createCategory(postDTO);
        return ResponseBuilder.buildResponse("201", "Success Created", category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> updateCategory(@Valid @PathVariable Long id, @RequestBody CategoryDTO postDTO) {
        CategoryDTO category = categoryService.updateCategory(id, postDTO);
        return ResponseBuilder.buildResponse("200", "Success Updated", category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> deleteCategory(@PathVariable Long id) {
        CategoryDTO category = categoryService.deleteCategory(id);
        return ResponseBuilder.buildResponse("200", "Success Deleted", category, HttpStatus.OK);
    }
}