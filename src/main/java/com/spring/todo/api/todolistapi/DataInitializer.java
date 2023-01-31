package com.spring.todo.api.todolistapi;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Logger;

import com.spring.todo.api.todolistapi.entity.User;
import com.spring.todo.api.todolistapi.repositories.UserRepository;

// This class is used to initialize the database with some test data.
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;

    Logger logger = Logger.getLogger(DataInitializer.class.getName());

    // This method saves two users to the database.
    @Override
    public void run(String... args) throws Exception {
        this.users.save(User.builder()
                .username("user")
                .password(generateRandomPassword(10))
                .roles(Arrays.asList("ROLE_USER"))
                .build());

        this.users.save(User.builder()
                .username("admin")
                .password(generateRandomPassword(10))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build());

        logger.info("Password user: " + users.findByUsername("user"));
        logger.info("Password admin: " + users.findByUsername("admin"));

    }

    public static String generateRandomPassword(int len) {
        // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of loop randomly chooses a character from the given

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

}
