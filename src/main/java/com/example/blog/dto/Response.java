package com.example.blog.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok akan menghasilkan getter dan setter untuk semua field
@AllArgsConstructor // Lombok akan menghasilkan konstruktor dengan semua argumen
@NoArgsConstructor // Lombok akan menghasilkan konstruktor tanpa argumen
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private String code;
    private String message;
    private T data;
}
