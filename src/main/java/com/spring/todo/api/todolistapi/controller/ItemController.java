package com.spring.todo.api.todolistapi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.todo.api.todolistapi.service.ItemService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.spring.todo.api.todolistapi.Exceptions.ItemNotFoundException;
import com.spring.todo.api.todolistapi.entity.Item;

// This class works as a controller to handle the requests and responses
// it creates the endpoints and injects the ItemService class to access the business logic

@RestController
@RequestMapping(ItemController.BASE_URL)
public class ItemController {

    public static final String BASE_URL = "/api/v1/items";

    @Autowired
    private ItemService itemService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // POST
    @PostMapping("/saveItems")
    public Item saveItem(@RequestBody Item item) {
        logger.info("ItemController.saveItem(): " + item.toString());
        return itemService.saveItem(item);
    }

    // GET
    @GetMapping("/getAllItems")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/getItemById/{id}")
    public Item getItemById(@PathVariable int id) throws ItemNotFoundException {


            
            Item item = itemService.getItemById(id);
            if (item == null) {
                throw new ItemNotFoundException("Item not found for this id :: " + id);
            }
            return item;

       
    }

    @GetMapping("/getItemByStatus/{status}")
    public List<Item> getItemByStatus(@PathVariable String status) {
        return itemService.getItemByStatus(status);
    }

    // PUT
    @PutMapping("/updateItem")
    public Item updateItem(@RequestBody Item item) {
        System.out.println("Updated");
        return itemService.updateItem(item);
    }

    // DELETE
    @DeleteMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable int id) {
        return itemService.deleteItem(id);
    }

}
