package com.example.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.auth.domain.user.User;
import com.example.auth.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) throws CustomException {
            if (user == null) throw new CustomException("User cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getLogin() == null) throw new CustomException("User Login cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getRole() == null) throw new CustomException("User role cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getId() == null) throw new CustomException("User ID cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getPassword() == null) throw new CustomException("User password cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getAuthorities() == null) throw new CustomException("User auth cannot be null", HttpStatus.BAD_REQUEST.value());
            if (user.getUsername() == null) throw new CustomException("User user name cannot be null", HttpStatus.BAD_REQUEST.value());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
