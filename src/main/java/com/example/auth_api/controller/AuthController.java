package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @GetMapping("/login")
    public ResponseEntity<AuthStatusResponse> logIn(@RequestParam String username, @RequestParam String password) {
        AuthStatusResponse response = authService.logIn(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Bearer Token", response.getToken());
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/register")
    public AuthStatusResponse signUp(@RequestParam String username, @RequestParam String password) {
        return authService.userRegistration(username, password);
    }


}
