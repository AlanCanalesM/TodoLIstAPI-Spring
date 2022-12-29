package com.spring.todo.api.todolistapi.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;


@Data
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String item;
    private String status;

    


}
