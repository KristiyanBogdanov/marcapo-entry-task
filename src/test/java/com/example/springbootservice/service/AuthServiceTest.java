package com.example.springbootservice.service;

import com.example.springbootservice.auth.CacheStorage;
import com.example.springbootservice.auth.JwtUtil;
import com.example.springbootservice.dto.AuthResponse;
import com.example.springbootservice.dto.LoginRequest;
import com.example.springbootservice.exception.EntityAlreadyExistsException;
import com.example.springbootservice.exception.InvalidCredentialsException;
import com.example.springbootservice.model.user.Permission;
import com.example.springbootservice.model.user.User;
import com.example.springbootservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CacheStorage cacheStorage;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User savedUser;
    private LoginRequest loginData;

    @BeforeEach
    void setUp() {
        savedUser = User.builder()
                .id("testMongoId")
                .username("testUser")
                .password("testHashedPassword")
                .userInfo(null)
                .permissions(List.of(Permission.READ_HELLO_WORLD))
                .build();

        loginData = LoginRequest.builder()
                .username("testUser")
                .password("password")
                .build();
    }

    @Test
    void signUp_ShouldCreateUser_WhenUsernameIsAvailable() {
        User signUpData = User.builder()
                .username(savedUser.getUsername())
                .password("password")
                .userInfo(savedUser.getUserInfo())
                .permissions(savedUser.getPermissions())
                .build();

        when(userRepository.findByUsername(signUpData.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpData.getPassword())).thenReturn(savedUser.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateAccessToken(savedUser.getId())).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(savedUser.getId())).thenReturn("refreshToken");

        AuthResponse response = authService.signUp(signUpData);

        assertAll(() -> {
            verify(passwordEncoder, times(1)).encode("password");
            verify(userRepository, times(1)).save(signUpData);
            verify(cacheStorage, times(1)).add(savedUser.getId(), savedUser.getPermissions());
        });

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void signUp_ShouldThrowException_WhenUsernameIsTaken() {
        when(userRepository.findByUsername(savedUser.getUsername())).thenReturn(Optional.of(savedUser));

        assertThrows(EntityAlreadyExistsException.class, () -> authService.signUp(savedUser));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signIn_ShouldReturnTokens_WhenCredentialsAreValid() {
        when(userRepository.findByUsername(savedUser.getUsername())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches("password", savedUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateAccessToken(savedUser.getId())).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(savedUser.getId())).thenReturn("refreshToken");

        AuthResponse response = authService.signIn(loginData);

        verify(cacheStorage, times(1)).add(savedUser.getId(), savedUser.getPermissions());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void signIn_ShouldThrowException_WhenCredentialsAreInvalid() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.signIn(loginData));

        verify(cacheStorage, never()).add(anyString(), anyList());
    }

    @Test
    void refreshToken_ShouldGenerateNewTokens() {
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("newRefreshToken");

        AuthResponse response = authService.refreshToken(savedUser.getId());

        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
    }

    @Test
    void signOut_ShouldRemoveUserFromCache() {
        when(cacheStorage.remove(savedUser.getId())).thenReturn(true);

        authService.signOut(savedUser.getId());

        verify(cacheStorage, times(1)).remove(savedUser.getId());
    }
}
