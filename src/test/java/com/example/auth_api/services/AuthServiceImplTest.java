package com.example.auth_api.services;

import com.example.auth_api.constants.RoleType;
import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.RegistrationRequest;
import com.example.auth_api.models.UserDto;
import com.example.auth_api.services.feign.UserServiceApi;
import com.example.auth_api.services.security.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationProvider authenticationProvider;
    @Mock
    private UserServiceApi userServiceApi;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;


    private RegistrationRequest registrationRequest;
    private UserDto userDto;
    private UserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        registrationRequest = RegistrationRequest.builder()
                .username("testUser")
                .password("password")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("testUser")
                .password("encodedPassword")
                .role(RoleType.USER.value())
                .build();

        userDetails = mock(UserDetails.class);
        authentication = mock(Authentication.class);
    }

    @Test
    void userRegistration_success() {
        when(userServiceApi.findByName("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        AuthStatusResponse response = authService.userRegistration(registrationRequest);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("User has been successfully registered", response.getState());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void userRegistration_WhenUserAlreadyExists() {
        when(userServiceApi.findByName("testUser")).thenReturn(Optional.of(userDto));

        AuthStatusResponse response = authService.userRegistration(registrationRequest);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
        assertEquals("User with this username already exist", response.getState());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void login_successAuthorization() {
        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userServiceApi.findByName("testUser")).thenReturn(Optional.of(userDto));
        when(jwtService.generateJwtToken(any(UserDetails.class), any(Long.class))).thenReturn("token");

        AuthStatusResponse response = authService.logIn("testUser", "password");
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("User has been authorized", response.getState());
        assertEquals("token", response.getToken());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void login_failedAuthorization() {
        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        AuthStatusResponse response = authService.logIn("testUser", "wrongPassword");

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getCode());
        assertEquals("Incorrect username or password is specified", response.getState());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void isUserExist_exists() {
        when(userServiceApi.findByName("testUser")).thenReturn(Optional.of(userDto));
        assertTrue(authService.isUserExist("testUser"));
    }

    @Test
    void isUserExist_notExists() {
        when(userServiceApi.findByName("testUser")).thenReturn(Optional.empty());
        assertFalse(authService.isUserExist("testUser"));
    }
}