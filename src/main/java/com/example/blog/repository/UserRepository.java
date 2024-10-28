package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blog.dto.auth.LoginDTO;
import com.example.blog.model.User;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT new com.example.blog.dto.auth.LoginDTO(u.username, u.password) FROM User u WHERE u.email = :email")
    Optional<LoginDTO> findUsernameAndPasswordByEmail(@Param("email") String email);
}
 