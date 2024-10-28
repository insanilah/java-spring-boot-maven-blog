package com.example.blog.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = TitleValidator.class) // Menunjuk ke kelas validator
@Target({ ElementType.FIELD, ElementType.METHOD }) // Bisa digunakan di field atau method
@Retention(RetentionPolicy.RUNTIME) // Digunakan pada runtime
public @interface ValidTitle {
    String message() default "Title must start with a capital letter"; // Pesan default
    Class<?>[] groups() default {}; // Bagian dari standar Bean Validation
    Class<? extends Payload>[] payload() default {}; // Bagian dari standar Bean Validation
}
