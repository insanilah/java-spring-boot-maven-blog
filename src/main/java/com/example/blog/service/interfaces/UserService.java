package com.example.blog.service.interfaces;
import com.example.blog.dto.auth.LoginDTO;
import com.example.blog.dto.auth.RegisterDTO;
import com.example.blog.model.User;

public interface UserService {
    User registerUser(RegisterDTO registerDTO);
    String loginUser(LoginDTO loginDTO);
    User findOrCreateUser(String email, String name);
}