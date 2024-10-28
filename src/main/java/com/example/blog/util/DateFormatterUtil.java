package com.example.blog.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    // Custom formatter for Indonesian date format
    private static final DateTimeFormatter INDONESIAN_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    // Method to format LocalDateTime to String
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(INDONESIAN_FORMATTER);
    }
}
