package com.spring.todo.api.todolistapi.controller;

import com.spring.todo.api.todolistapi.repositories.UserRepository;
import com.spring.todo.api.todolistapi.Security.JwtTokenProvider;
import com.spring.todo.api.todolistapi.entity.AuthenticationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

// This class is used to authenticate the user and generate a JWT token.
// It uses the AuthenticationManager to authenticate the user and the JwtTokenProvider to generate the JWT token.

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    public static final String BASE_URL = "/api/v1/items";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    // This method creates and endpoint for the user to authenticate.
    // It takes the username and password from the request body and uses the
    // AuthenticationManager to authenticate the user.
    // If the user is authenticated successfully, it generates a JWT token using the
    // JwtTokenProvider.
    // It then returns the username and the token in the response body.
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            var authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(authentication);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}
