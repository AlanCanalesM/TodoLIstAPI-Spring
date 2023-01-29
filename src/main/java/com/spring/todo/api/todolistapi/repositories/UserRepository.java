package com.spring.todo.api.todolistapi.repositories;

import com.spring.todo.api.todolistapi.entity.User;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This interface is used to perform CRUD operations on the User entity.
// It extends the JpaRepository interface which provides the basic CRUD operations.
@Hidden
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByUsername(String username);

}
