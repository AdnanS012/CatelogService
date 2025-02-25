package org.example.categoryservice;

import org.example.categoryservice.DTO.RestaurantDTO;
import org.example.categoryservice.Models.Restaurant;
import org.example.categoryservice.Repository.RestaurantRepository;
import org.example.categoryservice.Service.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;


    private Restaurant restaurant;
    private RestaurantDTO restaurantDTO;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "Description");
        restaurantDTO = new RestaurantDTO(1L, "KFC", "New York", "Fast Food", "+124567654", "Description");
    }

    @Test
    void testCreateRestaurant() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurantDTO);

        assertNotNull(createdRestaurant);
        assertEquals("KFC", createdRestaurant.getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testGetRestaurantById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Optional<RestaurantDTO> retrievedRestaurant = restaurantService.getRestaurantById(1L);

        assertNotNull(retrievedRestaurant);
        assertEquals("KFC", retrievedRestaurant.get().getName());
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(99L));
    }

    @Test
    void testGetRestaurantById_ReturnsOptional() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Optional<RestaurantDTO> retrievedRestaurant = restaurantService.getRestaurantById(1L);

        assertTrue(retrievedRestaurant instanceof Optional);
        assertTrue(retrievedRestaurant.isPresent());
        assertEquals("KFC", retrievedRestaurant.get().getName());
    }
    @Test
    void testDeleteRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository, times(1)).delete(restaurant);
    }

    @Test
    void testDeleteRestaurant_NotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.deleteRestaurant(99L));
    }
}
