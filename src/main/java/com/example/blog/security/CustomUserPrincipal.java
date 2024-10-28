package com.example.blog.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.blog.model.User;

public class CustomUserPrincipal implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementasikan logika sesuai kebutuhan
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementasikan logika sesuai kebutuhan
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implementasikan logika sesuai kebutuhan
    }

    @Override
    public boolean isEnabled() {
        return true; // Implementasikan logika sesuai kebutuhan
    }
}
