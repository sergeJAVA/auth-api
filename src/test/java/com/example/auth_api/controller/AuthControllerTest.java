package com.example.auth_api.controller;

import com.example.auth_api.models.LoginRequest;
import com.example.auth_api.models.RegistrationRequest;
import com.example.auth_api.services.AuthService;
import com.example.auth_api.services.feign.UserServiceApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserServiceApi userServiceApi;

    @BeforeEach
    void setUp() {
        userServiceApi.deleteAll();
        RegistrationRequest registrationRequest = new RegistrationRequest("testUser", "password");
        authService.userRegistration(registrationRequest);
    }

    @Test
    void login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("User has been authorized"))
                .andExpect(cookie().exists("token"));
    }

    @Test
    void login_failure() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPass");
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.state").value("Incorrect username or password is specified"))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(cookie().doesNotExist("token"));
    }

    @Test
    void register_success() throws Exception{
        RegistrationRequest request = new RegistrationRequest("newUser", "pass");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("User has been successfully registered"));
    }

    @Test
    void register_failure() throws Exception {
        RegistrationRequest request = new RegistrationRequest("testUser", "password");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state").value("User with this username already exist"))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void logout_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Выход успешен"))
                .andExpect(cookie().maxAge("token", 0));
    }
}