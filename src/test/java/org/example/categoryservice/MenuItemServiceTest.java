package org.example.categoryservice;

import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Models.MenuItem;
import org.example.categoryservice.Models.Restaurant;
import org.example.categoryservice.Repository.MenuItemRepository;
import org.example.categoryservice.Repository.RestaurantRepository;
import org.example.categoryservice.Service.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private Restaurant restaurant;
    private MenuItem menuItem;
    private MenuItemDTO menuItemDTO;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "Description");
        menuItem = new MenuItem("Burger", "Delicious beef burger",  new BigDecimal("5.99"), restaurant);
        menuItemDTO = new MenuItemDTO(1L, "Burger", "Delicious beef burger", new BigDecimal("5.99"), 1L);
    }

    @Test
    void testCreateMenuItem() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItemDTO createdItem = menuItemService.createMenuItem(menuItemDTO,1L);

        assertNotNull(createdItem);
        assertEquals("Burger", createdItem.getName());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }
    @Test
    void testGetMenuItemById() {
        when(menuItemRepository.findByIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(menuItem));

        Optional<MenuItemDTO> retrievedItem = menuItemService.getMenuItemById(1L, 1L);

        assertTrue(retrievedItem.isPresent());
        assertEquals("Burger", retrievedItem.get().getName());
    }


    @Test
    void testGetMenuItemById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getMenuItemById(1L, 99L));
    }
    @Test
    void testUpdateMenuItem_InvalidRestaurant() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItemDTO updatedDTO = new MenuItemDTO(1L, "Updated Burger", "New description", new BigDecimal("6.99"), 2L);

        assertThrows(IllegalArgumentException.class, () -> menuItemService.updateMenuItem(2L, 1L, updatedDTO));
    }

    @Test
    void testDeleteMenuItem() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        menuItemService.deleteMenuItem(1L, 1L);

        verify(menuItemRepository, times(1)).delete(menuItem);
    }

    @Test
    void testDeleteMenuItem_NotFound() {
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deleteMenuItem(1L, 99L));
    }

}
