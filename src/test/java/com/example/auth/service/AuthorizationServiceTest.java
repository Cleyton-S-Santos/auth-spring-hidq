package com.example.auth.service;

import com.example.auth.domain.user.RegisterDTO;
import com.example.auth.exceptions.CustomException;
import com.example.auth.repositories.UserRepository;
import com.example.auth.service.mother.UserMother;
import com.example.auth.services.AuthorizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class AuthorizationServiceTest {

    @InjectMocks
    AuthorizationService authorizationService;
    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_userDetails() {
       Mockito.when(repository.findByLogin(Mockito.any())).thenReturn(UserMother.getUserDetailsValid());
        UserDetails response = authorizationService.loadUserByUsername("teste");

        Assertions.assertNotNull(response);
    }
}
