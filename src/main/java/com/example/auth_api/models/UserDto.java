package com.example.auth_api.models;

import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Set<String> roles;
}
