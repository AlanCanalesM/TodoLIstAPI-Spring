package com.spring.todo.api.todolistapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.todo.api.todolistapi.Exceptions.ItemNotFoundException;
import com.spring.todo.api.todolistapi.controller.ItemController;
import com.spring.todo.api.todolistapi.service.ItemService;
import org.mockito.Mockito;
import com.spring.todo.api.todolistapi.entity.Item;
import org.skyscreamer.jsonassert.JSONAssert;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

//This notation is used is used for unit testing Spring MVC application.
@WebMvcTest(ItemController.class)
@WithMockUser
public class TestItemService {

        // This is used to send HTTP requests to the controller
        @Autowired
        private MockMvc mockMvc;

        // MockBean is used to add mocks to a Spring ApplicationContext.
        // A mock of itemService is created and auto-wired into the
        // ItemController.
        @MockBean
        private ItemService itemService;

        // This is a mock list of items
        List<Item> mockItemsList = new ArrayList<Item>(
                        Arrays.asList(new Item(1, "Test Item", "Test status")));

        ObjectMapper ObjectMapper = new ObjectMapper();

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
                Item mockItem = new Item(1, "Test Item", "Test status");
                Item expectedItem = new Item(1, "Test Item Updated", "Test status Updated");
                Mockito.when(itemService.updateItem(mockItem)).thenReturn(expectedItem);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
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
                Mockito.when(itemService.deleteItem(1)).thenReturn(mockItem.getId() + " id item deleted");
                RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/items/deleteItem/1")
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
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }

        // This is a method to test the getAllItems() method when there are no items
        // It requests the endpoint /api/v1/items/getAllItems and its mocked to return a null
        // and expects a 404 NOT FOUND status code
        @Test
        public void testGetAllNonExistentsItems() throws Exception {
                
                Mockito.when(itemService.getAllItems()).thenReturn(null);
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getAllItems")
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }


        // This is a method to test the getItemByStatus() method when there are no items
        // It requests the endpoint /api/v1/items/getItemByStatus/ with and invalid status
        // and expects a 404 NOT FOUND status code
        @Test
        public void testGetItemsByInvalidStatus() throws Exception{
                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getItemByStatus/InvalidStatus")
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
                                .content("{\"id\":1,\"item\":\"Test Item\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        //This is a method to test the updateItem() method with a non existent id
        // It requests the endpoint /api/v1/items/updateItem
        // and expects a 404 NOT FOUND status code

        @Test
        public void testUpdateItemWithNonExistentId() throws ItemNotFoundException, Exception {
                Mockito.when(itemService.updateItem(Mockito.any(Item.class))).thenThrow(new ItemNotFoundException("Item not found"));
                RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/items/updateItem")
                                .accept(MediaType.APPLICATION_JSON)
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
                                .accept(MediaType.APPLICATION_JSON);
                MvcResult result = mockMvc.perform(requestBuilder).andReturn();
                MockHttpServletResponse response = result.getResponse();
                assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        }
}