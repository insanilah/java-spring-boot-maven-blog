package com.example.blog.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.blog.model.User;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.interfaces.UserService;
import com.example.blog.util.ResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public HomeController(JwtUtil jwtUtil, UserService userService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> home() {
        return ResponseBuilder.buildResponse("200", "Success", null, HttpStatus.OK);
    }

    @GetMapping("/admin/beranda/v2")
    public Principal user(Principal principal) {
        System.out.println("username : " + principal.getName());
        return principal;
    }

    @GetMapping("/admin/beranda")
    public ResponseEntity<?> oauth2Callback(OAuth2AuthenticationToken authentication) {
        if (authentication != null) {
            String email = authentication.getPrincipal().getAttribute("email");
            String name = authentication.getPrincipal().getAttribute("name");
            User user = userService.findOrCreateUser(email, name);
            String token = jwtUtil.generateToken(user);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("token", token);
            return ResponseBuilder.buildResponse("200", "Success", responseData, HttpStatus.OK);
        } else {
            return ResponseBuilder.buildResponse("401", "Authentication failed", null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/admin/role")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authHeader); // Log header untuk melihat token yang diterima

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // log.info("Current Authentication: {}", authentication.toString()); // Log detail authentication
        log.info("Current roles: {}", roles); // Log semua roles

        if (!roles.isEmpty() && !roles.get(0).equals("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Akses ditolak: Anda tidak memiliki izin.");
        }

        String msg = "endpoint ini hanya role admin yg bisa akses. Current roles: " + roles;
        return ResponseBuilder.buildResponse("200", msg, null, HttpStatus.OK);
    }
}
