package com.spring.todo.api.todolistapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import com.spring.todo.api.todolistapi.entity.User;
import com.spring.todo.api.todolistapi.repositories.UserRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;

    
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        this.users.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER"))
                .build());

        this.users.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build());

        
    }

}
