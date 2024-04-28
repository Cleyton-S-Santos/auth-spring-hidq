package com.example.auth.service;

import com.example.auth.domain.user.User;
import com.example.auth.exceptions.CustomException;
import com.example.auth.service.mother.UserMother;
import com.example.auth.services.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class TokenServiceTest {

    @InjectMocks
    TokenService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_ValidUser_ReturnsToken() throws CustomException {
        ReflectionTestUtils.setField(service, "secret", "secret");
        User user = UserMother.getValidUserBody();
        String token = service.generateToken(user);
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 0);
    }

    @Test
    void generateToken_ExceptionThrown_ReturnsEmptyString() {
        ReflectionTestUtils.setField(service, "secret", "secret");
        User user = new User();
        Assertions.assertThrows(CustomException.class, () -> service.generateToken(user));
    }

    @Test
    void validateToken_ValidToken_ReturnsSubject() throws CustomException {
        ReflectionTestUtils.setField(service, "secret", "secret");
        User user = UserMother.getValidUserBody();
        String token = service.generateToken(user);
        String subject = service.validateToken(token);
        Assertions.assertEquals("login", subject);
    }

    @Test
    void validateToken_InvalidToken_ReturnsEmptyString() {
        ReflectionTestUtils.setField(service, "secret", "secret");
        String invalidToken = "invalid_token";
        String subject = service.validateToken(invalidToken);
        Assertions.assertEquals("", subject);
    }

    @Test
    public void testGenerateTokenWithNullLogin() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }

    @Test
    public void testGenerateTokenWithNullRole() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }

    @Test
    public void testGenerateTokenWithNullId() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }

    @Test
    public void testGenerateTokenWithNullPassword() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }

    @Test
    public void testGenerateTokenWithNullAuth() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }

    @Test
    public void testGenerateTokenWithNullUserName() {
        TokenService tokenService = new TokenService();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getLogin()).thenReturn(null);
        Assertions.assertThrows(CustomException.class, () -> {
            tokenService.generateToken(user);
        });
    }
}
