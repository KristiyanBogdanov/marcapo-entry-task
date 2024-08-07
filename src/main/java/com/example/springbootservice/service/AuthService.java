package com.example.springbootservice.service;

import com.example.springbootservice.auth.CacheStorage;
import com.example.springbootservice.auth.JwtUtil;
import com.example.springbootservice.dto.AuthResponse;
import com.example.springbootservice.dto.LoginRequest;
import com.example.springbootservice.exception.EntityAlreadyExistsException;
import com.example.springbootservice.exception.InvalidCredentialsException;
import com.example.springbootservice.model.user.User;
import com.example.springbootservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheStorage cacheStorage;
    private final JwtUtil jwtUtil;

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private AuthResponse generateTokens(String userId) {
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse signUp(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new EntityAlreadyExistsException("Username is already taken");
        });

        user.setPassword(hashPassword(user.getPassword()));
        userRepository.save(user);

        return generateTokens(user.getId());
    }

    public AuthResponse signIn(LoginRequest loginData) {
        User user = userRepository
                .findByUsername(loginData.getUsername())
                .filter(entity -> passwordEncoder.matches(loginData.getPassword(), entity.getPassword()))
                .orElseThrow(InvalidCredentialsException::new);

        /*
            @Note: Another possible solution is to use two separate caches - one acting as a blacklist and the other as a whitelist.
            When a user signs out, we add the user to the blacklist cache and remove the user from the whitelist cache.
            When a user signs in, we check if the user is in the blacklist cache. If the user is in the blacklist cache, we reject the request.
            The idea of whitelisting is to store user permissions and reduce the number of database queries.
        */
        cacheStorage.add(user.getId(), user.getPermissions());

        return generateTokens(user.getId());
    }

    public AuthResponse refreshToken(String userId) {
        return generateTokens(userId);
    }

    public void signOut(String userId) {
        cacheStorage.remove(userId);
    }
}
