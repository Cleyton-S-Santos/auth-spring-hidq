package com.example.auth.controllers;

import com.example.auth.domain.user.*;
import com.example.auth.exceptions.CustomException;
import com.example.auth.exceptions.ErrorResponse;
import com.example.auth.services.TokenService;
import com.example.auth.repositories.UserRepository;
import com.example.auth.services.ProcessLoginService;
import com.example.auth.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    ProcessLoginService processLoginService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais invalidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "ok, retorno do jwt",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) throws CustomException {
        Authentication auth = processLoginService.process(data);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Informações invalidas ao registrar ou usuario já registrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "ok, retorno da entidade criada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) throws CustomException {
        userService.validateUserData(data);
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, UserRole.USER);

        User user =  this.repository.save(newUser);

        return ResponseEntity.ok().body(user);
    }
}
