package com.spring.todo.api.todolistapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spring.todo.api.todolistapi.Exceptions.ItemNotFoundException;
import com.spring.todo.api.todolistapi.service.ItemService;
import org.mockito.Mockito;
import com.spring.todo.api.todolistapi.entity.Item;
import org.skyscreamer.jsonassert.JSONAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.springframework.http.HttpStatus;

//This class is used to test the ItemController class

//This notation is used to tell Spring that this is a test class
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestItemController {

        // This is used to send HTTP requests to the controller
        @Autowired
        private MockMvc mockMvc;

        // MockBean is used to add mocks to a Spring ApplicationContext.
        // A mock of itemService is created and auto-wired into the
        // ItemController.
        @MockBean
        private ItemService itemService;

        String tokenUser;
        String tokenAdmin;
        String invalidTestToken = "invalidToken";

        @Autowired
        WebApplicationContext applicationContext;

        @Autowired
        ObjectMapper objectMapper;

        // This is a mock list of items
        List<Item> mockItemsList = new ArrayList<Item>(
                        Arrays.asList(new Item(1, "Test Item", "Test status")));

        ObjectMapper ObjectMapper = new ObjectMapper();

        // This method is used to get the tokens for the user and admin
        // it sends a POST request to the /api/v1/items/signin endpoint
        // and gets the token from the response
        @BeforeAll
        public void getJwTokens() throws Exception {

                String body = "{\"username\":\"" + "user" + "\", \"password\":\"" + "password" + "\"}";

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/items/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk()).andReturn();

                tokenUser = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

                String bodyadmin = "{\"username\":\"" + "admin" + "\", \"password\":\"" + "password" + "\"}";

                MvcResult resultadmin = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/items/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyadmin))
                                .andExpect(status().isOk()).andReturn();

                tokenAdmin = JsonPath.read(resultadmin.getResponse().getContentAsString(), "$.token");
        }

        // This is a test method to test the saveItems method in the ItemController
        // it mocks the saveItem method in the ItemService class
        // then it sends a POST request to the saveItems endpoint
        // at the end it checks if the response is the same as the expected response
        // and if the status code is 200 == OK
        @Test
        public void testSaveItems() throws Exception {
                Item mockItem = new Item(1, "Test Item", "Test status");
                Mockito.when(itemService.saveItem(Mockito.any(Item.class))).thenReturn(mockItem);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/items/saveItems")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                System.out.println(result.getResponse());
                String expected = ObjectMapper.writeValueAsString(mockItem);
                JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
                assertEquals(HttpStatus.OK.value(), response.getStatus());

        }

        // This is a test method to test the getAllItems() method in the ItemController
        // it mocks the itemService.getAllItems() method and returns the mockItem list
        // then it creates a request to the endpoint /api/v1/items/getAllItems
        // at the end it compares the expected and actual response
        @Test
        public void testGetAllItems() throws Exception {
                Mockito.when(itemService.getAllItems()).thenReturn(mockItemsList);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getAllItems")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);

                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                System.out.println(result.getResponse());
                String expected = "[{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}]";
                JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        }

        // This is a test method to test the getItemById() method in the ItemController
        // it mocks the itemService.getItemById() method and returns the item with id 1
        // from the mockItem list
        // then it creates a request to the endpoint /api/v1/items/getItemById/1
        // at the end it compares the expected and actual response
        @Test
        public void testGetItemById() throws Exception {
                Mockito.when(itemService.getItemById(1)).thenReturn(mockItemsList.get(0));
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getItemById/1")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);

                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                System.out.println(result.getResponse());
                String expected = "{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}";
                JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),
                                false);
        }

        // This is a test method to test the getItemByStatus() method in the
        // ItemController
        // it mocks the itemService.getItemByStatus() method and returns the item with
        // status "Test status" from the mockItem list
        // then it creates a request to the endpoint /api/v1/items/getItemByStatus/Test
        // status
        // at the end it compares the expected and actual response
        @Test
        public void testGetItemByStatus() throws Exception {
                Mockito.when(itemService.getItemByStatus("Test status")).thenReturn(mockItemsList);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getItemByStatus/Test status")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                System.out.println(result.getResponse());
                String expected = "[{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}]";
                JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),
                                false);
        }

        // This is a method to test the updateItem() method in the ItemController
        // it mocks the itemService.updateItem() method and returns the updated item
        // then it creates a request to the endpoint /api/v1/items/updateItem
        // at the end it compares the expected and actual response
        // and if the status code is 200 == OK
        @Test
        public void testUpdateItem() throws Exception {
                Item mockItem = new Item(1,"Test Item", "Test status");
                Item expectedItem = new Item(1, "Test Item Updated", "Test status Updated");
                Mockito.when(itemService.updateItem(mockItem)).thenReturn(expectedItem);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":1,\"item\":\"Test Item Updated\",\"status\":\"Test status Updated\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                System.out.println(result.getResponse());
                assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        // This is a method to test the deleteItem() method in the ItemController
        // it mocks the itemService.deleteItem() method and returns the message "{id} id
        // item deleted" of the deleted
        // at the end it compares the expected and actual response
        @Test
        public void testDeleteItem() throws Exception {
                Item mockItem = new Item(1, "Test Item", "Test status");
                Mockito.when(itemService.getItemById(1)).thenReturn(mockItem);
                Mockito.when(itemService.deleteItem(1)).thenReturn("1 id item deleted");
                RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/items/deleteItem/1")
                                .header("Authorization", "Bearer " + tokenAdmin)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                System.out.println(result.getResponse());
                String expected = "1 id item deleted";
                assertEquals(expected, result.getResponse().getContentAsString());


        }

        // UnHappy Path Tests

        // This is a method to test the saveItem() method without field item
        // It requests the endpoint /api/v1/items/saveItems
        // and expects a 400 BAD REQUEST status code
        @Test
        public void testSaveItemWithoutItem() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/items/saveItems")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":1,\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        // This is a method to test GetItemById() method with an invalid id
        // It requests the endpoint /api/v1/items/getItemById/700
        // and expects a 404 NOT FOUND status code
        @Test
        public void testGetNotFoundItemById() throws Exception {

                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getItemById/700")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // This is a method to test the getAllItems() method when there are no items
        // It requests the endpoint /api/v1/items/getAllItems and its mocked to return a
        // null
        // and expects a 404 NOT FOUND status code
        @Test
        public void testGetAllNonExistentsItems() throws Exception {

                Mockito.when(itemService.getAllItems()).thenReturn(null);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getAllItems")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // This is a method to test the getItemByStatus() method when there are no items
        // It requests the endpoint /api/v1/items/getItemByStatus/ with and invalid
        // status
        // and expects a 404 NOT FOUND status code
        @Test
        public void testGetItemsByInvalidStatus() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders
                                .get("/api/v1/items/getItemByStatus/InvalidStatus")
                                .header("Authorization", "Bearer " + tokenUser)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // This is a method to test the UpdateItem() method without field id
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 400 BAD REQUEST status code
        @Test
        public void testUpdateItemWithoutId() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"item\":\"Test Item\",\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        // This is a method to test the updateItem() method without field item
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 400 BAD REQUEST status code
        @Test
        public void testUpdateItemWithoutItem() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":1,\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        // This is a method to test the updateItem() method without field status
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 400 BAD REQUEST status code
        @Test
        public void testUpdateItemWithoutStatus() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":1,\"item\":\"Test Item\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        // This is a method to test the updateItem() method with a non existent id
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 404 NOT FOUND status code

        @Test
        public void testUpdateItemWithNonExistentId() throws ItemNotFoundException, Exception {
                Mockito.when(itemService.updateItem(Mockito.any(Item.class)))
                                .thenThrow(new ItemNotFoundException("Item not found"));
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + tokenUser)
                                .content("{\"id\":718300,\"item\":\"Test Item\",\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // This is a method to test the deleteItem() method with a non existent id
        // It requests the endpoint /api/v1/items/deleteItem/71830
        // and expects a 404 NOT FOUND status code
        @Test
        public void testDeleteItemWithNonExistentId() throws Exception {

                RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/items/deleteItem/71830")
                                .header("Authorization", "Bearer " + tokenAdmin)
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // Test methods without Authorization

        // This is a method to test the saveItems() method without Authorization
        // It requests the endpoint /api/v1/items/saveItems
        // and expects a 401 UNAUTHORIZED status code
        @Test
        public void testSaveItemsWithoutAuthorization() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/items/saveItems")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidTestToken)
                                .content("{\"item\":\"Test Item\",\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        }

        // This is a method to test the getAllItems() method without Authorization
        // It requests the endpoint /api/v1/items/getAllItems
        // and expects a 401 UNAUTHORIZED status code
        @Test
        public void testGetAllItemsWithoutAuthorization() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getAllItems")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidTestToken)
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        }

        // This is a method to test the getItemById() method without Authorization
        // It requests the endpoint /api/v1/items/getItemById/1
        // and expects a 401 UNAUTHORIZED status code
        @Test
        public void testGetItemByIdWithoutAuthorization() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getItemById/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidTestToken)
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        }

        // This is a method to test the updateItem() method without Authorization
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 401 UNAUTHORIZED status code
        @Test
        public void testUpdateItemWithoutAuthorization() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidTestToken)
                                .content("{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        }

        // This is a method to test the deleteItem() method without Authorization
        // It requests the endpoint /api/v1/items/deleteItem/1
        // and expects a 401 UNAUTHORIZED status code
        @Test
        public void testDeleteItemWithoutAuthorization() throws Exception {
                RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/items/deleteItem/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidTestToken)
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        }
}