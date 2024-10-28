package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blog.dto.auth.LoginDTO;
import com.example.blog.dto.auth.RegisterDTO;
import com.example.blog.model.Role;
import com.example.blog.model.User;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.CustomUserPrincipal;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.interfaces.UserService;
import com.example.blog.util.AuthUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, com.example.blog.repository.RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());

        Role role = roleRepository.findByRoleName("USER") // Pastikan ada metode ini di RoleRepository
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public String loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found"));

        // Ambil role dari pengguna dan buat otoritas
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

        log.info("user:{}", user.getRole().getRoleName());
        log.info("authorities:{}", authorities);
// Autentikasi menggunakan AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword(),
                        authorities
                )
        );

        // Buat instance CustomUserDetails
        CustomUserPrincipal customUserDetails = new CustomUserPrincipal(user.getUsername(), user.getPassword(), authorities);

        // Generate JWT token setelah autentikasi berhasil
        return jwtUtil.generateToken(customUserDetails);
    }

    @Override
    public User findOrCreateUser(String email, String name) {
        // Cek apakah pengguna sudah ada di database berdasarkan email
        Optional<LoginDTO> loginData = userRepository.findUsernameAndPasswordByEmail(email);
        User newUser = new User();

        if (loginData.isPresent()) {
            newUser.setUsername(loginData.get().getUsername());
            newUser.setPassword(loginData.get().getPassword());
            return newUser;
        }

        // Jika belum ada, buat pengguna baru
        newUser.setEmail(email);
        newUser.setName(name);
        // Generate a secure random password
        String randomPassword = AuthUtil.generateRandomPassword();
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        newUser = userRepository.save(newUser);
        return newUser;
    }

}
