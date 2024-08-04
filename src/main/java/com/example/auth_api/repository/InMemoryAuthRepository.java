package com.example.auth_api.repository;

import com.example.auth_api.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryAuthRepository implements AuthRepository{

    private final List<User> users = new ArrayList<>();


    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public User save(User user) {

        user.setId(System.currentTimeMillis());
        users.add(user);

        return user;
    }

    @Override
    public User update(User user) {
        User updatedUser = findById(user.getId()).orElse(null);
        if (updatedUser != null){
            updatedUser.setId(user.getId());
            updatedUser.setName(user.getName());
            updatedUser.setPassword(user.getPassword());
        }

        return updatedUser;
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(users::remove);
    }
}
