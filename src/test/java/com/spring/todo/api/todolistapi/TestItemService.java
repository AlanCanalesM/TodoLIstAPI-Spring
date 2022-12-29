package com.spring.todo.api.todolistapi;

import com.spring.todo.api.todolistapi.controller.ItemController;
import com.spring.todo.api.todolistapi.service.ItemService;
import org.mockito.Mockito;
import com.spring.todo.api.todolistapi.entity.Item;
import org.skyscreamer.jsonassert.JSONAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ItemController.class)
@WithMockUser
public class TestItemService {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    List<Item> mockItem = new ArrayList<Item>(
            Arrays.asList(new Item(1, "Test Item", "Test status"))
    );
    String exampleItem = "{\"id\":1,\"name\":\"Test Item\",\"status\":\"Test status\"}";

    @Test
    public void testGetAllItems() throws Exception {
        Mockito.when(itemService.getAllItems()).thenReturn(mockItem);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/items/getAllItems").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse());
        String expected = "[{\"id\":1,\"item\":\"Test Item\",\"status\":\"Test status\"}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    
}
