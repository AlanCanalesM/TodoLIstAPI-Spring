package com.spring.todo.api.todolistapi.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

// This class is used to return the user information.
@Hidden
@RestController()
@RequestMapping(UserInfoController.BASE_URL)
@Tag(name = "service", description = "the book API with description tag annotation")
public class UserInfoController {

    public static final String BASE_URL = "/api/v1/items";
   

    // This method creates an endpoint to return the user information.
    // It uses the @AuthenticationPrincipal annotation to get the user information.
    // It then returns the username and the roles in the response body.
    @SuppressWarnings("rawtypes")
    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> ((GrantedAuthority) a).getAuthority())
                .collect(toList()));
                
        return ok(model);
    }

}
