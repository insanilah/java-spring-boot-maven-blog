package com.example.blog.mapper;

import com.example.blog.dto.UserDTO;
import com.example.blog.model.User;

public class UserMapper {
    
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return user;
    }
}
