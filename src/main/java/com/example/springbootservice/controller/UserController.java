package com.example.springbootservice.controller;

import com.example.springbootservice.model.User;
import com.example.springbootservice.model.UserInfo;
import com.example.springbootservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PutMapping("/{id}")
    public User updateUserInfo(@PathVariable String id, @Valid @RequestBody UserInfo userInfo) {
        return userService.updateUserInfo(id, userInfo);
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        userService.deleteById(id);
    }
}
