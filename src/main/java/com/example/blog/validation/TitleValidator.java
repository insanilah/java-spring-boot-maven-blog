package com.example.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<ValidTitle, String> {

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        // Cek apakah title dimulai dengan huruf kapital
        return title != null && Character.isUpperCase(title.charAt(0));
    }
}
