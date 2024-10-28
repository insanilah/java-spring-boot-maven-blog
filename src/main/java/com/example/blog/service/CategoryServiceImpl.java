package com.example.blog.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.blog.dto.CategoryDTO;
import com.example.blog.mapper.CategoryMapper;
import com.example.blog.model.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.service.interfaces.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Map<String, Object> getAllCategories(int page, int size, String[] sort) {
        // Parsing sorting input
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        String sortBy = sort[0];

        Page<Category> category = categoryRepository.findAllByIsDeletedFalse(PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
        Page<CategoryDTO> categoryDTO = category.map(CategoryMapper::toDTO);

        // Membuat response
        Map<String, Object> data = new HashMap<>();
        data.put("posts", categoryDTO.getContent());
        data.put("totalPages", categoryDTO.getTotalPages());
        data.put("totalElements", categoryDTO.getTotalElements());
        data.put("currentPage", categoryDTO.getNumber() + 1);
        data.put("pageSize", categoryDTO.getSize());

        return data;
    }

    @Override
    public Map<String, Object> getAllCategoriesByName(String name, int page, int size, String[] sort) {
        // Parsing sorting input
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        String sortBy = sort[0];

        Page<Category> category = categoryRepository.findCategorysByNameIsDeletedFalse(name, PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
        Page<CategoryDTO> categoryDTO = category.map(CategoryMapper::toDTO);

        // Membuat response
        Map<String, Object> data = new HashMap<>();
        data.put("posts", categoryDTO.getContent());
        data.put("totalPages", categoryDTO.getTotalPages());
        data.put("totalElements", categoryDTO.getTotalElements());
        data.put("currentPage", categoryDTO.getNumber() + 1);
        data.put("pageSize", categoryDTO.getSize());

        return data;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        CategoryDTO categoryDTO = CategoryMapper.toDTO(category);
        return categoryDTO;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Cek apakah post dengan name yang sama sudah ada
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with the same name already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category categorySave = categoryRepository.save(category);
        CategoryDTO CategoryToDTO = CategoryMapper.toDTO(categorySave);

        return CategoryToDTO;
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(categoryDTO.getName());

        Category categorySave = categoryRepository.save(category);
        CategoryDTO CategoryToDTO = CategoryMapper.toDTO(categorySave);

        return CategoryToDTO;
    }

    @Override
    @Transactional
    public CategoryDTO deleteCategory(Long id) {
        // Temukan kategori
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        log.info("Category details: {}", category); // Logging detail kategori

        // Tandai kategori sebagai dihapus
        category.setIsDeleted(true);
        categoryRepository.save(category);

        // Kembalikan DTO kategori
        return CategoryMapper.toDTO(category);
    }

}
