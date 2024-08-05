package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    public AuthStatusResponse logIn(String username, String password) {
        AuthStatusResponse response;
        if (authService.isLogInSuccess(username, password)) {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User has been authorized")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .state("Incorrect user name or password is specified")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        return response;
    }

}
