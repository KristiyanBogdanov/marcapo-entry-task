package com.example.springbootservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/helloworld")
public class HelloWorldController {
    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).READ_HELLO_WORLD.name())")
    @GetMapping()
    public String helloWorld() {
        return "helloworld";
    }
}
