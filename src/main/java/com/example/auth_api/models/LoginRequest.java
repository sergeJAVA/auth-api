package com.example.auth_api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class LoginRequest {

    @NonNull
    private String username;
    @NotBlank(message = "Password must not be blank")
    private String password;
}
