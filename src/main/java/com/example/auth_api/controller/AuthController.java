package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/login")
    public AuthStatusResponse logIn(@RequestParam String username, @RequestParam String password) {
        return authService.logIn(username, password);
    }

    @GetMapping("/register")
    public AuthStatusResponse signUp(@RequestParam String username, @RequestParam String password) {

        return authService.userRegistration(username, password);
    }


}
