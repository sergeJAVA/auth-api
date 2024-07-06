package com.example.auth_api.controller;

import com.example.auth_api.models.User;
import com.example.auth_api.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

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








}
