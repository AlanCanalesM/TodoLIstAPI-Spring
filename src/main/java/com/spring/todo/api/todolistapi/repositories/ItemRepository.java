package com.spring.todo.api.todolistapi.repositories;

import com.spring.todo.api.todolistapi.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Item findById(int id);

    List<Item> findByStatus(String status);

}
