package com.example.springbootservice.model;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    READ_USER,
    UPDATE_USER,
    DELETE_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
