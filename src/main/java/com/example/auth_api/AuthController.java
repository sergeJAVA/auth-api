package com.example.auth_api;

import com.example.auth_api.models.User;
import com.example.auth_api.services.UserService;
import lombok.AllArgsConstructor;
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
    public List<User> users(){
        return userService.findAll();
    }

    @GetMapping("/find/{id}")
    public User userById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PostMapping("/create")
    public User createUser(User user){
        return userService.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id){
        userService.deleteById(id);
    }








}
