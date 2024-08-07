package com.example.springbootservice.service;

import com.example.springbootservice.auth.CacheStorage;
import com.example.springbootservice.exception.EntityNotFoundException;
import com.example.springbootservice.model.user.Permission;
import com.example.springbootservice.model.user.User;
import com.example.springbootservice.model.user.UserInfo;
import com.example.springbootservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheStorage cacheStorage;

    @InjectMocks
    private UserService userService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = User.builder()
                .id("testMongoId")
                .username("testUser")
                .password("testHashedPassword")
                .userInfo(null)
                .permissions(List.of(Permission.READ_HELLO_WORLD))
                .build();
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));

        User foundUser = userService.findById(savedUser.getId());

        verify(userRepository, times(1)).findById(savedUser.getId());
        assertEquals(savedUser, foundUser);
    }


    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        String userId = "nonExistingId";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUserInfo_ShouldUpdateAndReturnUpdatedUser_WhenUserExists() {
        UserInfo newUserInfo = UserInfo.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .build();

        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User updatedUser = userService.updateUserInfo(savedUser.getId(), newUserInfo);

        verify(userRepository, times(1)).save(savedUser);
        assertEquals(updatedUser.getUserInfo(), newUserInfo);
    }

    @Test
    void updateUserInfo_ShouldThrowException_WhenUserNotFound() {
        String userId = "nonExistingId";
        UserInfo newUserInfo = UserInfo.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUserInfo(userId, newUserInfo));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteById_ShouldDeleteUserAndRemoveFromCache_WhenUserExists() {
        doNothing().when(userRepository).deleteById(savedUser.getId());
        when(cacheStorage.remove(savedUser.getId())).thenReturn(true);

        userService.deleteById(savedUser.getId());

        verify(userRepository, times(1)).deleteById(savedUser.getId());
        verify(cacheStorage, times(1)).remove(savedUser.getId());
    }

}
