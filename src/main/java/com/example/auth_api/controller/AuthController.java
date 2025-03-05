package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.LoginRequest;
import com.example.auth_api.models.RegistrationRequest;
import com.example.auth_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<AuthStatusResponse> signIn(@Valid @RequestBody LoginRequest request) {
        return logIn(request.getUsername(), request.getPassword());
    }


    private ResponseEntity<AuthStatusResponse> logIn(@RequestParam String username, @RequestParam String password) {
        try {
            AuthStatusResponse response = authService.logIn(username, password);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Bearer Token", response.getToken());
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", username, e);
            AuthStatusResponse errorResponse = AuthStatusResponse.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .state("Incorrect username or password is specified")
                    .timestamp(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<AuthStatusResponse> signUp(@RequestBody RegistrationRequest request) {
        AuthStatusResponse authStatusResponse = authService.userRegistration(request);
        return new ResponseEntity<>(authStatusResponse, HttpStatus.valueOf(authStatusResponse.getCode()));
    }


}
