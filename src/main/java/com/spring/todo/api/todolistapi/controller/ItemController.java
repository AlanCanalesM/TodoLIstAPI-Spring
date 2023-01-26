package com.spring.todo.api.todolistapi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.spring.todo.api.todolistapi.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.spring.todo.api.todolistapi.Exceptions.ItemNotFoundException;
import com.spring.todo.api.todolistapi.Exceptions.ItemWithoutAllParamsException;
import com.spring.todo.api.todolistapi.Security.JwtTokenProvider;
import com.spring.todo.api.todolistapi.entity.AuthenticationRequest;
import com.spring.todo.api.todolistapi.entity.Item;

import static org.springframework.http.ResponseEntity.ok;
// This class works as a controller to handle the requests and responses
// it creates the endpoints and injects the ItemService class to access the business logic

@RestController
@RequestMapping(ItemController.BASE_URL)
@RequiredArgsConstructor
public class ItemController {

    public static final String BASE_URL = "/api/v1/items";

    @Autowired
    private ItemService itemService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    // This method creates and endpoint for the user to authenticate.
    // It takes the username and password from the request body and uses the
    // AuthenticationManager to authenticate the user.
    // If the user is authenticated successfully, it generates a JWT token using the
    // JwtTokenProvider.
    // It then returns the username and the token in the response body.
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            var authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(authentication);
            Map<Object, Object> model = new HashMap<>();
            // model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    // POST
    @PostMapping("/saveItems")
    public Item saveItem(@RequestBody Item item) throws ItemWithoutAllParamsException {
        logger.info("ItemController.saveItem(): " + item.toString());
        Item item1 = itemService.getItemById(item.getId());
        if (item.getItem() == null || item.getStatus() == null) {
            throw new ItemWithoutAllParamsException("Bad Request");
        } else if (item1 != null) {
            throw new ItemWithoutAllParamsException("Item already exists, it can't be created");

        } else {
            return itemService.saveItem(item);
        }

    }

    // GET
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
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
    public List<Item> getItemByStatus(@PathVariable String status) throws ItemNotFoundException {

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
        if (item.getId() == 0 || item.getItem() == null || item.getStatus() == null) {
            throw new ItemWithoutAllParamsException("Bad Request");
        } else {

            // if (itemService.updateItem(item) == null) {
            // throw new ItemNotFoundException("Item not found for this id :: " +
            // item.getId());
            // }
            try {
                Item itemupdated = itemService.updateItem(item);
                return itemupdated;
            } catch (Exception e) {

                throw new ItemNotFoundException("Item not found for this id :: " + item.getId());
            }

        }

    }

    // DELETE
    @DeleteMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable int id) throws ItemNotFoundException {

        if (itemService.deleteItem(id) == null) {
            throw new ItemNotFoundException("Item not found for this id :: " + id);
        }
        return itemService.deleteItem(id);

    }

}
