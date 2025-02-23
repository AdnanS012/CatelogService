package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.RestaurantDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RestaurantService {
    List<RestaurantDTO> getAllRestaurants();
    Optional<RestaurantDTO> getRestaurantById(Long id);
    RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO);
    Optional<RestaurantDTO> updateRestaurant(Long id, RestaurantDTO restaurantDTO);
    void deleteRestaurant(Long id);
}
