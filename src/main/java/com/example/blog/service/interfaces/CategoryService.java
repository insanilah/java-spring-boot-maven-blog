package com.example.blog.service.interfaces;
import java.util.Map;
import com.example.blog.dto.CategoryDTO;

public interface CategoryService {
    Map<String, Object> getAllCategories(int page, int size, String[] sort);
    Map<String, Object> getAllCategoriesByName(String name, int page, int size, String[] sort);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long id);
}