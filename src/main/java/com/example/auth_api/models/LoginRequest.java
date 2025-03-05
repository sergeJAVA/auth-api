package com.example.auth_api.models;


import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class LoginRequest {

    private String username;
    private String password;
}
