package com.spring.todo.api.todolistapi.repositories;

import com.spring.todo.api.todolistapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This interface is used to perform CRUD operations on the User entity.
// It extends the JpaRepository interface which provides the basic CRUD operations.
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByUsername(String username);

}
