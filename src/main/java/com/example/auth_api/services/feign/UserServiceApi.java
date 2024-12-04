package com.example.auth_api.services.feign;

import com.example.auth_api.models.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "user-api", url = "${feign.user-service.url}")
public interface UserServiceApi {

    @GetMapping("/findId/{id}")
    UserDto userById(@PathVariable Long id);

    @GetMapping("/find/{username}")
    Optional<UserDto> findByName(@PathVariable String username);

    @PostMapping("/create")
    UserDto createUser(@RequestBody UserDto userDto);



}
