package com.example.auth_api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class AuthStatusResponse {
    private String state;
    private Integer code;
    private LocalDateTime timestamp;
}
