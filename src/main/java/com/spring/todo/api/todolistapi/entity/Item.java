package com.spring.todo.api.todolistapi.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;



// This class is used to define the structure of the table in the database and the fields in the table
// it also defines the arguments to each item in the table

@Data
@AllArgsConstructor
@Entity
@Table(name = "items_tbl")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "item")
    private String item;

    @Column(name = "status")
    private String status;

    


}
