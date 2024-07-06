package com.example.auth_api.repository;

import com.example.auth_api.models.User;

import java.util.List;
import java.util.Optional;

public interface AuthRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);


}
