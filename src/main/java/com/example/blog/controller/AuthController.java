package com.example.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.auth.LoginDTO;
import com.example.blog.dto.auth.RegisterDTO;
import com.example.blog.service.interfaces.NotificationService;
import com.example.blog.service.interfaces.UserService;
import com.example.blog.util.ResponseBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final NotificationService notificationService;

    public AuthController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        userService.registerUser(registerDTO);
        notificationService.sendUserRegistrationNotification(registerDTO);
        return ResponseBuilder.buildResponse("200", "Success Register", null, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.loginUser(loginDTO);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("token", token);

        return ResponseBuilder.buildResponse("200", "Success", responseData, HttpStatus.OK);
    }


}
