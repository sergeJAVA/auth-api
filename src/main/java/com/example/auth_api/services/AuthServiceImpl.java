package com.example.auth_api.services;

import com.example.auth_api.constants.RoleType;
import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.RegistrationRequest;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationProvider authenticationProvider;
    private final UserServiceApi userServiceApi;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;



    @SneakyThrows
    @Override
    public AuthStatusResponse userRegistration(RegistrationRequest request) {
        AuthStatusResponse response;

        if (isUserExist(request.getUsername())) {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .state("User with this username already exist")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            userServiceApi.createUser(UserDto.builder()
                    .name(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Set.of(RoleType.USER.value()))
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
        try {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Long userId = userServiceApi.findByName(username).get().getId();

            String token = jwtService.generateJwtToken(userDetails, userId);


            return AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User has been authorized")
                    .timestamp(LocalDateTime.now())
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", username, e);
            return AuthStatusResponse.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .state("Incorrect username or password is specified")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }


    boolean isUserExist(String username) {
        return userServiceApi.findByName(username).isPresent();
    }
}
