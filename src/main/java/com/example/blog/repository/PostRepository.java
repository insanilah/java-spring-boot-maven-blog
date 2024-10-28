package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.example.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    // contoh "query method di Spring Data JPA" untuk mencari post berdasarkan title
    Optional<Post> findByTitle(String title);

    @NonNull
    @Override
    Page<Post> findAll(@NonNull Pageable pageable);

    // Custom query menggunakan JPQL untuk mencari post berdasarkan judul
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Post> findPostsByTitle(@Param("title") String title, Pageable pageable);
}
