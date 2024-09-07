package com.example.auth_api.services.feign;

import com.example.auth_api.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "user-api", url = "${feign.user-service.url}")
public interface UserServiceApi {

    @GetMapping("/findId/{id}")
    User userById(@PathVariable Long id);

    @GetMapping("/find/{username}")
    Optional<User> findByName(@PathVariable String username);

    @PostMapping("/create")
    User createUser(User user);

}
