package com.spring.todo.api.todolistapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import com.spring.todo.api.todolistapi.entity.Item;
import com.spring.todo.api.todolistapi.repositories.ItemRepository;
import com.spring.todo.api.todolistapi.service.ItemService;

// This class tests the ItemService class
@SpringBootTest
@AutoConfigureMockMvc
public class TestItemService {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private Item item;

    private List<Item> itemsList;

    // This method will run before each test
    // It will create a new item
    // also create a list of items
    @BeforeEach
    public void setUp() {
        item = new Item();
        item.setId(1);
        item.setItem("Test Item");
        item.setStatus("Active");

        itemsList = new ArrayList<>(
                Arrays.asList(
                        new Item(1, "Test Item 1", "Active"),
                        new Item(2, "Test Item 2", "Active"),
                        new Item(3, "Test Item 3", "Active")));

    }

    // Test for saveItem method
    // It will check if the item is saved or not
    @Test
    public void testSaveItem() {
        when(itemRepository.save(item)).thenReturn(item);
        assertEquals(item, itemService.saveItem(item));
    }

    // Test for getAllItems method
    // It will check if the list of items is returned or not
    // It will check if the list of items has the correct size
    // It will check if the list of items is not null
    @Test
    public void testGetAllItems() {
        Item item1 = new Item(1, "Test Item 1", "Active");

        given(itemRepository.findAll()).willReturn(List.of(item, item1));

        List<Item> items = itemService.getAllItems();

        assertThat(items).isNotNull();
        assertThat(items).hasSize(2);
    }

    // Test for getItemById method
    // It will check if the item is returned or not
    // It will check if the item has the correct id
    // It will check if the item is not null
    // It will check if the item is equal to the item returned by the method
    @Test
    public void testGetItemById() {

        given(itemRepository.findById(1)).willReturn(item);

        Item item = itemService.getItemById(1);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(1);
        assertEquals(item, itemService.getItemById(1));
    }

    // Test for getItemByStatus method
    // It will check if the list of items is returned or not
    // It will check if the list of items has the correct size
    // It will check if the list of items is not null
    // It will check if the list of items is equal to the list of items returned by
    // the method
    @Test
    public void testGetItemByStatus() {

        given(itemRepository.findByStatus("Active")).willReturn(itemsList);

        List<Item> items = itemService.getItemByStatus("Active");

        assertThat(items).isNotNull();
        assertThat(items).hasSize(3);
        assertEquals(items, itemService.getItemByStatus("Active"));
    }

    // Test for updateItem method
    // It will check if the item is updated or not
    // It will check if the item is equal to the item returned by the method

    @Test
    public void testUpdateItem() {
        Item itemToUpdate = new Item(1, "Test Item 1", "Active");
        Item itemUpdated = new Item(1, "Test Item Updated", "Updated");
        when(itemRepository.findById(1)).thenReturn(itemToUpdate);
        when(itemRepository.save(itemUpdated)).thenReturn(itemUpdated);
        assertEquals(itemUpdated, itemService.updateItem(itemUpdated));
    }

    // Test for deleteItem method
    // It will check if the item is deleted or not
    // It will check if the item is equal to the item returned by the method
    @Test
    public void testDeleteItem() {
        
        when(itemRepository.deleteById(1)).thenReturn("1 id item deleted");
        assertEquals("1 id item deleted", itemService.deleteItem(1));
    }

}
