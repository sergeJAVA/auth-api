package com.example.auth_api.services;

import com.example.auth_api.constants.RoleType;
import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.UserDto;
import com.example.auth_api.services.feign.UserServiceApi;
import com.example.auth_api.services.security.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationProvider authenticationProvider;
    private final UserServiceApi userServiceApi;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    private boolean isLogInSuccess(String password, UserDto userDto) {
        return passwordEncoder.matches(password, userDto.getPassword());
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
                    .password(passwordEncoder.encode(password))
                    .role(RoleType.USER.value())
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

        UserDto userDto = userServiceApi.findByName(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        if (isLogInSuccess(password, userDto)) {

            response = AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User has been authorized")
                    .timestamp(LocalDateTime.now())
                    .token(getToken(username, password, userDto))
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

    private String getToken(String username, String password, UserDto userDto) {
        return jwtService.generateJwtToken(new User(
                username,
                password,
                List.of(new SimpleGrantedAuthority(userDto.getRole()))));
    }

    private boolean isUserExist(String username) {
        return userServiceApi.findByName(username).isPresent();
    }
}
