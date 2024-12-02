package com.example.auth_api.services;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.UserDto;
import com.example.auth_api.services.feign.UserServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceApi userServiceApi;

    private boolean isLogInSuccess(String username, String password) {
        if (userServiceApi.findByName(username)
                .filter(user -> user.getPassword().equals(password))
                .isPresent()) {
            return true;
        }
        return false;
    }

    @SneakyThrows
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
            userServiceApi.createUser(UserDto.builder()
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
        return userServiceApi.findByName(username).isPresent();
    }
}
