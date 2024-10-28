package com.example.blog.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.blog.model.UserActivity;

public interface UserActivityRepository extends MongoRepository<UserActivity, String> {
    List<UserActivity> findByUsername(String username);
}
