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
import org.springframework.web.bind.annotation.*;


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

        AuthStatusResponse response = authService.logIn(username, password);

        if (response.getCode() == HttpStatus.OK.value()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Bearer Token", response.getToken());
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


    @PostMapping("/register")
    public ResponseEntity<AuthStatusResponse> signUp(@RequestBody RegistrationRequest request) {
        AuthStatusResponse authStatusResponse = authService.userRegistration(request);
        return new ResponseEntity<>(authStatusResponse, HttpStatus.valueOf(authStatusResponse.getCode()));
    }


}
