package com.example.springbootservice.service;

import com.example.springbootservice.auth.CacheStorage;
import com.example.springbootservice.exception.EntityNotFoundException;
import com.example.springbootservice.model.user.User;
import com.example.springbootservice.model.user.UserInfo;
import com.example.springbootservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CacheStorage cacheStorage;

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateUserInfo(String id, UserInfo userInfo) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        existingUser.setUserInfo(userInfo);
        return userRepository.save(existingUser);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
        cacheStorage.remove(id);
    }
}
