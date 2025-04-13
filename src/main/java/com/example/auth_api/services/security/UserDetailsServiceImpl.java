package com.example.auth_api.services.security;

import com.example.auth_api.services.feign.UserServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceApi userServiceApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userServiceApi.findByName(username)
                .map(userDto ->
                        new User(
                                userDto.getName(),
                                userDto.getPassword(),
                                userDto.getRoles().stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toSet())
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("Error! Customer not found!"));
    }
}
