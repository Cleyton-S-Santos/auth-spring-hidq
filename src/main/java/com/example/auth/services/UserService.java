package com.example.auth.services;

import com.example.auth.domain.user.RegisterDTO;
import com.example.auth.exceptions.CustomException;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateUserData(RegisterDTO data) throws CustomException {
        StringBuilder error = new StringBuilder();

        if (repository.findByLogin(data.login()) != null) {
            error.append("User already registered. ");
        }
        if (data.login().isBlank()) {
            error.append("Login is blank. ");
        }
        if (data.password().isBlank()) {
            error.append("Password is blank. ");
        }

        if (error.length() > 0) {
            throw new CustomException(error.toString(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
