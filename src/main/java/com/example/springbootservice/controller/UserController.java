package com.example.springbootservice.controller;

import com.example.springbootservice.model.user.User;
import com.example.springbootservice.model.user.UserInfo;
import com.example.springbootservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).READ_USER.name())")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).UPDATE_USER.name())")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserInfo(@PathVariable String id, @Valid @RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userService.updateUserInfo(id, userInfo));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).DELETE_USER.name())")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
