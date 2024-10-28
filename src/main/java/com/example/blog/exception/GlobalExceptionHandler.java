package com.example.blog.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.blog.dto.Response;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull org.springframework.http.HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        // Ambil FieldError pertama (jika ada)
        FieldError firstError = ex.getBindingResult().getFieldErrors().get(0);
        String errorMessage = firstError.getDefaultMessage();

        // Buat respons custom dengan pesan error pertama
        Response<String> response = new Response<>("400", errorMessage, null);
        // Mengembalikan response entity dengan status BAD_REQUEST
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException ex,
            @NonNull org.springframework.http.HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        // Buat respons custom dengan pesan error pertama
        String errorMessage = "The requested URL was not found or the request method is not supported.";
        Response<String> response = new Response<>("404", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NonNull org.springframework.http.HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        String errorMessage = "The requested HTTP method is not supported for this URL.";
        Response<String> response = new Response<>("405", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Response<String>> handleResponseStatusException(ResponseStatusException ex) {
        // Membuat respons dengan format yang diinginkan
        Response<String> response = new Response<>(String.valueOf(ex.getStatusCode().value()), ex.getReason(), null);
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Response<String> response = new Response<>("402", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Response<String> response = new Response<>("401", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Start Security Exception
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response<String>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.info("error msg: {}", ex.getMessage());
        Response<String> response = new Response<>("403", "JWT Expired", null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<String>> handleExpiredJwtException(BadCredentialsException ex) {
        log.info("error msg: {}", ex.getMessage());
        Response<String> response = new Response<>(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "The username or password is incorrect", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<Response<String>> handleExpiredJwtException(AccountStatusException ex) {
        log.info("error msg: {}", ex.getMessage());
        Response<String> response = new Response<>("403", "You are not authorized to access this resource", null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Response<String>> handleExpiredJwtException(SignatureException ex) {
        log.info("error msg: {}", ex.getMessage());
        Response<String> response = new Response<>("403", "The JWT signature is invalid", null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    // End Security Exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleAllExceptions(Exception ex) {
        log.info("error msg: {}", ex.getMessage());
        Response<String> response = new Response<>("500", "Internal Server Error", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
