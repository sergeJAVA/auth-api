package com.example.auth_api.services;

import com.example.auth_api.models.User;
import com.example.auth_api.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthRepository authRepository;

    @Override
    public List<User> findAll() {
        return authRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return authRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return authRepository.save(user);
    }

    @Override
    public User update(User user) {
        return authRepository.update(user);
    }

    @Override
    public void deleteById(Long id) {
        authRepository.deleteById(id);
    }
}
