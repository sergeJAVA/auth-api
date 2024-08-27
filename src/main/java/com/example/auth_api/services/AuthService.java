package com.example.auth_api.services;


import com.example.auth_api.models.AuthStatusResponse;

public interface AuthService {

    AuthStatusResponse userRegistration(String username, String password);

    AuthStatusResponse logIn(String username, String password);
}
