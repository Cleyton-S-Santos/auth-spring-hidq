package com.example.auth.services;

import com.example.auth.domain.user.AuthenticationDTO;
import com.example.auth.exceptions.CustomException;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProcessLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository repository;


    public Authentication process(AuthenticationDTO data) throws CustomException {
        UserDetails user = repository.findByLogin(data.login());
        if(user == null){
            throw new CustomException("Usuario não encontrado", HttpStatus.NOT_FOUND.value());
        }
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
       try{
           return this.authenticationManager.authenticate(usernamePassword);
       } catch (Exception e){
        throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
       }
    }
}
