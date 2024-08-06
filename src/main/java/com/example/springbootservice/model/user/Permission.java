package com.example.springbootservice.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    READ_USER,
    UPDATE_USER,
    DELETE_USER,
    READ_HELLO_WORLD,
    GENERATE_PRODUCTS,
    READ_PRODUCT,
    SEARCH_PRODUCTS_BY_NAME,
    SEARCH_PRODUCTS_BY_PRICE,
    SEARCH_PRODUCTS_BY_LAUNCH_DATE,
    SEARCH_PRODUCTS_BY_COORDINATES;

    @Override
    public String getAuthority() {
        return name();
    }
}
