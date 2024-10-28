package com.example.blog.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.blog.dto.Response;

public class ResponseBuilder {

    // Method untuk membangun response yang dapat digunakan di banyak controller
    public static <T> ResponseEntity<Response<T>> buildResponse(String code, String message, T data, HttpStatus status) {
        Response<T> response = new Response<>(code, message, data);
        return new ResponseEntity<>(response, status);
    }
}
