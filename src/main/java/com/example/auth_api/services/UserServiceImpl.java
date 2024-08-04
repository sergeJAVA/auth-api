package com.example.auth_api.services;

import com.example.auth_api.models.User;
import com.example.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User updatedUser = new User();
        if (user != null){
            updatedUser.setName(user.getName());
            updatedUser.setPassword(user.getPassword());
        }
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }
}
