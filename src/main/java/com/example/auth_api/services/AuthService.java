package com.example.auth_api.services;


import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.RegistrationRequest;

public interface AuthService {

    AuthStatusResponse userRegistration(RegistrationRequest request);

    AuthStatusResponse logIn(String username, String password);
}
