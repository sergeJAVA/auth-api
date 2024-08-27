package com.example.auth_api.services;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.User;
import com.example.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private boolean isLogInSuccess(String username, String password) {
        if (Objects.isNull(userRepository.findByName(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null))) {
            return false;
        }
        return true;
    }

    @Override
    public AuthStatusResponse userRegistration(String username, String password) {
        AuthStatusResponse response;

        if (isUserExist(username)) {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User with this username already exist")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            userRepository.save(User.builder()
                    .name(username)
                    .password(password)
                    .build());

            response = AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User has been successfully registered")
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        return response;
    }

    @Override
    public AuthStatusResponse logIn(String username, String password) {
        AuthStatusResponse response;
        if (isLogInSuccess(username, password)) {
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

    private boolean isUserExist(String username) {
        return userRepository.findByName(username).isPresent();
    }
}
