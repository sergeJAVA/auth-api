package com.example.auth_api.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class User {
    private String name;
    private String password;
    private Long id;

}
