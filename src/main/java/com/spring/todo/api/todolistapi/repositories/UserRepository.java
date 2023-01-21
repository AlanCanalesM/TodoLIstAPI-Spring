package com.spring.todo.api.todolistapi.repositories;
import java.util.Optional;
import com.spring.todo.api.todolistapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public class UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
}
