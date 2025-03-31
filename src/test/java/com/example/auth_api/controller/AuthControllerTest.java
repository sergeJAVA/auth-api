package com.example.auth_api.controller;

import com.example.auth_api.config.TestSecurityConfig;
import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.LoginRequest;
import com.example.auth_api.models.RegistrationRequest;
import com.example.auth_api.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    private RegistrationRequest registrationRequest;

    private LoginRequest loginRequest;

    private AuthStatusResponse authStatusResponseLogInSuccess;

    private AuthStatusResponse authStatusResponseLogInFailure;

    private AuthStatusResponse authStatusResponseRegisterFailure;
    private AuthStatusResponse authStatusResponseRegisterSuccess;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        registrationRequest = RegistrationRequest.builder()
                .username("testUser")
                .password("password")
                .build();

        loginRequest = LoginRequest.builder()
                .username("testUser")
                .password("password")
                .build();

        authStatusResponseLogInSuccess = AuthStatusResponse.builder()
                .code(HttpStatus.OK.value())
                .state("User has been authorized")
                .token("testToken")
                .build();

        authStatusResponseLogInFailure = AuthStatusResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .state("Incorrect username or password is specified")
                .build();

        authStatusResponseRegisterSuccess = AuthStatusResponse.builder()
                .code(HttpStatus.OK.value())
                .state("User has been successfully registered")
                .timestamp(LocalDateTime.now())
                .build();

        authStatusResponseRegisterFailure = AuthStatusResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .state("User with this username already exist")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void register_success() throws Exception {
        when(authService.userRegistration(any(RegistrationRequest.class))).thenReturn(authStatusResponseRegisterSuccess);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("User has been successfully registered"));
        verify(authService, times(1)).userRegistration(any(RegistrationRequest.class));
    }

    @Test
    void register_failure() throws Exception {
        when(authService.userRegistration(any(RegistrationRequest.class))).thenReturn(authStatusResponseRegisterFailure);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(jsonPath("$.state").value("User with this username already exist"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_success() throws Exception {
        when(authService.logIn(loginRequest.getUsername(), loginRequest.getPassword())).thenReturn(authStatusResponseLogInSuccess);

        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("User has been authorized"))
                .andExpect(jsonPath("$.token").value("testToken"));
        verify(authService, times(1)).logIn(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Test
    void login_failure() throws Exception {
        when(authService.logIn(loginRequest.getUsername(), loginRequest.getPassword())).thenReturn(authStatusResponseLogInFailure);

        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.state").value("Incorrect username or password is specified"));
        verify(authService, times(1)).logIn(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Test
    void logout_success() throws Exception{
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("token", 0))
                .andExpect(jsonPath("$.message").value("Выход успешен"));
    }

}