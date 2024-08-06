package com.example.springbootservice.controller;

import com.example.springbootservice.dto.AuthResponse;
import com.example.springbootservice.dto.LoginRequest;
import com.example.springbootservice.model.user.User;
import com.example.springbootservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.signUp(user));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest loginData) {
        return ResponseEntity.ok(authService.signIn(loginData));
    }

    @GetMapping("/refresh")
    private ResponseEntity<AuthResponse> refreshToken(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(authService.refreshToken(userId));
    }

    @DeleteMapping("/signout")
    private ResponseEntity<Void> signOut(@AuthenticationPrincipal String userId) {
        authService.signOut(userId);
        return ResponseEntity.noContent().build();
    }
}
