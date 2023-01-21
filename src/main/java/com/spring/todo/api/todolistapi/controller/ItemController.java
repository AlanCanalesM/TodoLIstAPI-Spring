package com.spring.todo.api.todolistapi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.spring.todo.api.todolistapi.service.ItemService;

import jakarta.validation.constraints.Null;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.spring.todo.api.todolistapi.Exceptions.ItemNotFoundException;
import com.spring.todo.api.todolistapi.Exceptions.ItemWithoutAllParamsException;
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
    public Item saveItem(@RequestBody Item item) throws ItemWithoutAllParamsException{
        logger.info("ItemController.saveItem(): " + item.toString());
        Item item1 = itemService.getItemById(item.getId());
        if(item.getItem() == null || item.getStatus() == null){
            throw new ItemWithoutAllParamsException("Bad Request");
        }else if(item1 != null){
            throw new ItemWithoutAllParamsException("Item already exists, it can't be created");

        }else{
            return itemService.saveItem(item);
        }
        
        
    }

    // GET
    @GetMapping("/getAllItems")
    public List<Item> getAllItems() {
        List<Item> item = itemService.getAllItems();
        if (item == null) {
            throw new ItemNotFoundException("Items not found");
        }
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
    public List<Item> getItemByStatus(@PathVariable String status) throws ItemNotFoundException{

        List<Item> item = itemService.getItemByStatus(status);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Items not found for this status :: " + status);
        }
        return itemService.getItemByStatus(status);
    }

    // PUT
    @PutMapping("/updateItem")
    public Item updateItem(@RequestBody Item item) throws ItemNotFoundException, ItemWithoutAllParamsException {
        System.out.println("Updated");
        if(item.getId() == 0 || item.getItem() == null || item.getStatus() == null){
            throw new ItemWithoutAllParamsException("Bad Request");
        }else{
            
            // if (itemService.updateItem(item) == null) {
            //     throw new ItemNotFoundException("Item not found for this id :: " + item.getId());
            // }
            try{
                Item itemupdated = itemService.updateItem(item);
                return itemupdated;
                }
            catch(Exception e){
                
                throw new ItemNotFoundException("Item not found for this id :: " + item.getId());
            }
            
            
        }
        
        
    }

    // DELETE
    @DeleteMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable int id) throws ItemNotFoundException{

            
            if (itemService.deleteItem(id) == null) {
                throw new ItemNotFoundException("Item not found for this id :: " + id);
            }
            return itemService.deleteItem(id);
        
    }

}
