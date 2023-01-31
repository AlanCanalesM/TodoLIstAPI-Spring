package com.spring.todo.api.todolistapi.entity;

import lombok.Data;
import lombok.AllArgsConstructor;



import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;



// This class is used to define the structure of the table in the database and the fields in the table
// it also defines the arguments to each item in the table
@Hidden
@Data
@AllArgsConstructor
@Entity
@Table(name = "items_tbl")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @NotNull(message = "Item cannot be null")
    @Column(name = "item")
    @Pattern(regexp = "[a-zA-Z]*", message = "Item can only have letters and no spaces")
    private String item;

    @NotNull(message = "Status cannot be null")
    @Column(name = "status")
    @Pattern(regexp = "^[0-1]$", message = "Status must be a number 1 (Active) or 0 (Inactive) ")
    private String status;

    // This is a default constructor
    public Item() {
        
    }

}
