package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // contoh "query method di Spring Data JPA" untuk mencari post berdasarkan name
    Optional<Tag> findByName(String name);

    @NonNull
    @Override
    Page<Tag> findAll(@NonNull Pageable pageable);

    // Custom query menggunakan JPQL untuk mencari post berdasarkan judul
    // @Query("SELECT p FROM Tag p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    // List<Tag> findTagsByName(@Param("name") String name);

    // Custom query menggunakan JPQL untuk mencari post berdasarkan judul
    @Query("SELECT c FROM Tag c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Tag> findTagsByName(@Param("name") String name, Pageable pageable);
}
