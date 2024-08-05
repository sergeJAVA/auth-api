package com.example.auth_api.services;

import com.example.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    public boolean isLogInSuccess(String username, String password) {
        if (Objects.isNull(userRepository.findByName(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null))) {
            return false;
        }
        return true;
    }
}
