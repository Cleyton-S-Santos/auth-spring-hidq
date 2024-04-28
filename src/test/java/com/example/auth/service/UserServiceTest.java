package com.example.auth.service;

import com.example.auth.domain.user.RegisterDTO;
import com.example.auth.exceptions.CustomException;
import com.example.auth.repositories.UserRepository;
import com.example.auth.service.mother.UserMother;
import com.example.auth.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateUserData_UserAlreadyRegistered_ThrowsException() {
        RegisterDTO registerDTO = new RegisterDTO("existing_user", "password");
        Mockito.when(userRepository.findByLogin("existing_user")).thenReturn(UserMother.getUserDetailsValid()); // Mocking user already registered
        Assertions.assertThrows(CustomException.class, () -> userService.validateUserData(registerDTO));
    }

    @Test
    void validateUserData_LoginBlank_ThrowsException() {
        RegisterDTO registerDTO = new RegisterDTO("", "password");
        Assertions.assertThrows(CustomException.class, () -> userService.validateUserData(registerDTO));
    }

    @Test
    void validateUserData_PasswordBlank_ThrowsException() {
        RegisterDTO registerDTO = new RegisterDTO("username", "");
        Assertions.assertThrows(CustomException.class, () -> userService.validateUserData(registerDTO));
    }

    @Test
    void validateUserData_ValidData_NoExceptionThrown() {
        RegisterDTO registerDTO = new RegisterDTO("new_user", "password");
        Mockito.when(userRepository.findByLogin("new_user")).thenReturn(null);

        Assertions.assertDoesNotThrow(() -> userService.validateUserData(registerDTO));
    }
}
