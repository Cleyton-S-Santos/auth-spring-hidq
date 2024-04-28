package com.example.auth.service;

import com.example.auth.domain.user.AuthenticationDTO;
import com.example.auth.domain.user.User;
import com.example.auth.domain.user.UserRole;
import com.example.auth.exceptions.CustomException;
import com.example.auth.repositories.UserRepository;
import com.example.auth.service.mother.UserMother;
import com.example.auth.services.ProcessLoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public class ProcessLoginServiceTest {

    @InjectMocks
    ProcessLoginService processLoginService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void process_UserNotFound_ThrowsException() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("non_existing_user", "password");
        Mockito.when(repository.findByLogin("non_existing_user")).thenReturn(null);

        Assertions.assertThrows(CustomException.class, () -> processLoginService.process(authenticationDTO));
    }

    @Test
    void process_AuthenticationManagerThrowsException_ThrowsException() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("existing_user", "password");
        User user = UserMother.getValidUserBody();
        Mockito.when(repository.findByLogin("existing_user")).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new RuntimeException("Authentication failed"));

        Assertions.assertThrows(CustomException.class, () -> processLoginService.process(authenticationDTO));
    }

    @Test
    void process_ValidUser_ReturnsAuthentication() throws CustomException {
        // Arrange
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("existing_user", "password");
        User user =  UserMother.getValidUserBody();
        Mockito.when(repository.findByLogin("existing_user")).thenReturn(user);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

        Authentication result = processLoginService.process(authenticationDTO);

        Assertions.assertNotNull(result);
    }

}
