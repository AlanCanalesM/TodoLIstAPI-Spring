package com.spring.todo.api.todolistapi.repositories;

import com.spring.todo.api.todolistapi.entity.Item;

import org.springframework.data.repository.CrudRepository;




//This interface works as a repository to access the database with the Jpa's defined methods

public interface ItemRepository extends CrudRepository<Item, Integer>{

    //This method is used to get the item by status from the database using GET Method
    public Iterable<Item> findByStatus(String status);

    public Item findById(int id);

   
}



