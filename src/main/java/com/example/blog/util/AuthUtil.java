package com.example.blog.util;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthUtil {
    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[12]; // 12-byte password for example
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }    
}
