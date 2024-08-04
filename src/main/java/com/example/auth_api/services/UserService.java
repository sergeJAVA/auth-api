package com.example.auth_api.services;

import com.example.auth_api.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);

    void saveAll(List<User> users);
}
