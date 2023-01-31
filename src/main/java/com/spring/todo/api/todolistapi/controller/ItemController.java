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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    public ResponseEntity signin(@Valid @RequestBody AuthenticationRequest data) {

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved item successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "400", description = "Missing required fields or Item already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/saveItems")
    public Item saveItem(@Valid @RequestBody Item item) throws ItemWithoutAllParamsException {
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all items successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/getAllItems")
    public List<Item> getAllItems() {
        List<Item> item = itemService.getAllItems();
        if (item == null) {
            throw new ItemNotFoundException("Items not found");
        }
        return itemService.getAllItems();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get item successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing required field (id)", content = @Content),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/getItemById/{id}")
    public Item getItemById(@PathVariable("id") @NotNull @Pattern(regexp = "^[0-9]$", message = "Id must be a number") int id) throws ItemNotFoundException {

        Item item = itemService.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item not found for this id :: " + id);
        }
        return item;

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get item successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing required field (status)", content = @Content),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/getItemByStatus/{status}")
    public List<Item> getItemByStatus(@PathVariable("status") @NotNull @Pattern(regexp = "[a-zA-Z]*", message = "status can only have numbers") String status) throws ItemNotFoundException {

        List<Item> item = itemService.getItemByStatus(status);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Items not found for this status :: " + status);
        }
        return itemService.getItemByStatus(status);
    }

    // PUT
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "item updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing required fields (id, item, status)", content = @Content),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/updateItem")
    public Item updateItem(@Valid @RequestBody Item item) throws ItemNotFoundException, ItemWithoutAllParamsException {
        logger.info("Item Updated");
        if (item.getId() == 0 || item.getItem() == null || item.getStatus() == null) {
            throw new ItemWithoutAllParamsException("Bad Request");
        } else {

            try {
                Item itemupdated = itemService.updateItem(item);
                return itemupdated;
            } catch (Exception e) {

                throw new ItemNotFoundException("Item not found for this id :: " + item.getId());
            }

        }

    }

    // DELETE
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "item deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)) }),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing required field (id)", content = @Content),
            @ApiResponse(responseCode = "401", description = "UnAuthorized, You have to get the token from /signin endpoint and paste it into Barear Token field", content = @Content)
    })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable("id") @NotNull @Pattern(regexp = "^[0-9]$", message = "Id must be a number") int id) throws ItemNotFoundException {

        if (itemService.getItemById(id) == null) {
            throw new ItemNotFoundException("Item not found for this id :: " + id);
        }
        return itemService.deleteItem(id);

    }

}
