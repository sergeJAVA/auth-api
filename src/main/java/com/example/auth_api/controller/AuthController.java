package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.LoginRequest;
import com.example.auth_api.models.RegistrationRequest;
import com.example.auth_api.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<AuthStatusResponse> logIn(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        AuthStatusResponse authStatusResponse = authService.logIn(request.getUsername(), request.getPassword());

        if (authStatusResponse.getCode() == HttpStatus.OK.value()) {

            createCookie(response, authStatusResponse.getToken());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " + authStatusResponse.getToken());
            return new ResponseEntity<>(authStatusResponse, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(authStatusResponse, HttpStatus.FORBIDDEN);
    }


    @PostMapping("/register")
    public ResponseEntity<AuthStatusResponse> signUp(@RequestBody RegistrationRequest request) {
        AuthStatusResponse authStatusResponse = authService.userRegistration(request);
        return new ResponseEntity<>(authStatusResponse, HttpStatus.valueOf(authStatusResponse.getCode()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        deleteCookie(response);
        return ResponseEntity.ok(Map.of("message", "Выход успешен"));
    }

    private void createCookie(HttpServletResponse response, String token) {
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setHttpOnly(true); // Делаем куки HTTP-only
        tokenCookie.setSecure(true); // Только для HTTPS (в продакшене)
        tokenCookie.setPath("/"); // Доступно для всех путей
        tokenCookie.setMaxAge(7 * 24 * 60 * 60); // Время жизни куки: 7 дней
        response.addCookie(tokenCookie);
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie tokenCookie = new Cookie("token", null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(0); // Удаляем куки
        response.addCookie(tokenCookie);
    }

}
