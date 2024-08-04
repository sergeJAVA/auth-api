package com.example.auth_api.controller;

import com.example.auth_api.models.AuthStatusResponse;
import com.example.auth_api.models.User;
import com.example.auth_api.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String home(){
        return "index.html";
    }

    public AuthStatusResponse logIn(String username, String password) {
        AuthStatusResponse response;
        if (userService.isLogInSuccess(username, password)) {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.OK.value())
                    .state("User has been authorized")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            response = AuthStatusResponse.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .state("Incorrect user name or password is specified")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        return response;
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
        nikodim.setPassword("315z");

        users.add(nikodim);
        users.add(serega);

        userService.saveAll(users);
    }








}
