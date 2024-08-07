package com.example.springbootservice.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    READ_USER,
    UPDATE_USER,
    DELETE_USER,
    READ_HELLO_WORLD,
    GENERATE_PRODUCTS,
    CREATE_PRODUCT,
    READ_PRODUCT,
    SEARCH_PRODUCTS_BY_ALL_FIELDS,
    SEARCH_PRODUCTS_BY_NAME,
    SEARCH_PRODUCTS_BY_PRICE,
    SEARCH_PRODUCTS_BY_LAUNCH_DATE,
    SEARCH_PRODUCTS_BY_COORDINATES;

    @Override
    public String getAuthority() {
        return name();
    }
}
