package com.spring.todo.api.todolistapi.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.todo.api.todolistapi.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.spring.todo.api.todolistapi.entity.Item;
import java.util.*;

// This class works as a controller to handle the requests and responses
// it creates the endpoints and injects the ItemService class to access the business logic

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ItemController {

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
    public Item getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
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
