package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // contoh "query method di Spring Data JPA" untuk mencari post berdasarkan name
    Optional<Category> findByName(String name);

    @NonNull
    @Override
    Page<Category> findAll(@NonNull Pageable pageable);

    // Custom query menggunakan JPQL untuk mencari post berdasarkan judul
    // @Query("SELECT p FROM Category p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    // List<Category> findCategorysByName(@Param("name") String name);

    // Custom query menggunakan JPQL untuk mencari post berdasarkan judul
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.isDeleted = false")
    Page<Category> findCategorysByNameIsDeletedFalse(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    Page<Category> findAllByIsDeletedFalse(Pageable pageable);
}
