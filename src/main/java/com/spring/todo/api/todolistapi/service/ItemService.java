package com.spring.todo.api.todolistapi.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.todo.api.todolistapi.repositories.ItemRepository;
import com.spring.todo.api.todolistapi.entity.Item;
import java.util.*;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // This method is used to save the item in the database using POST Method
    public Item saveItem(Item item) {
        System.out.println("ItemService.saveItem(): " + item.toString());
        return itemRepository.save(item);
    }

    // This method is used to get all the items from the database using GET Method
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // This method is used to get the item by id from the database using GET Method
    public Item getItemById(int id) {
        return itemRepository.findById(id);
    }

    // This method is used to get the item by status from the database using GET
    // Method
    public List<Item> getItemByStatus(String status) {
        return itemRepository.findByStatus(status);
    }

    // This method updates the item in the database using PUT Method
    public Item updateItem(Item item) {
        System.out.println("Updates");
        Item itemToUpdate = itemRepository.findById(item.getId());
        itemToUpdate.setItem(item.getItem());
        itemToUpdate.setStatus(item.getStatus());
        return itemRepository.save(itemToUpdate);
    }

    // This method deletes the item by id using DELETE Method
    public String deleteItem(int id) {
        itemRepository.deleteById(id);
        return id + "id -> item deleted";
    }

}
