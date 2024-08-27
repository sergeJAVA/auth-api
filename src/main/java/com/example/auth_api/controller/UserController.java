package com.example.auth_api.controller;

import com.example.auth_api.models.User;
import com.example.auth_api.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String home(){
        return "index.html";
    }

    @GetMapping("/all-users")
    public List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/find/{id}")
    public User userById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/create")
    public User createUser(User user) {
        return userService.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostConstruct
    public void loadUser(){
        List<User> users = new ArrayList<>();

        User serega = new User();
        User nikodim = new User();

        serega.setName("Serega");
        serega.setPassword("12345");

        nikodim.setName("Nikodim");
        nikodim.setPassword("123876");

        users.add(nikodim);
        users.add(serega);

        userService.saveAll(users);
    }
}
