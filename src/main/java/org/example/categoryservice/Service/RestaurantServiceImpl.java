package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.RestaurantDTO;
import org.example.categoryservice.Models.Restaurant;
import org.example.categoryservice.Repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<RestaurantDTO> getRestaurantById(Long id) {
        return restaurantRepository.findById(id) //Safe handling
                .map(this::convertToDTO);
    }
    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getLocation(),
                restaurant.getCuisine(),
                restaurant.getContact(),
                restaurant.getDescription()
        );
    }

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant(
                null,
                restaurantDTO.getName(),
                restaurantDTO.getLocation(),
                restaurantDTO.getCuisine(),
                restaurantDTO.getContact(),
                restaurantDTO.getDescription()
        );
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantDTO(savedRestaurant.getId(), savedRestaurant.getName(), savedRestaurant.getLocation(),
                savedRestaurant.getCuisine(), savedRestaurant.getContact(), savedRestaurant.getDescription());
    }
    @Override
    public Optional<RestaurantDTO> updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        return restaurantRepository.findById(id).map(existingRestaurant -> {
            Restaurant updatedRestaurant = restaurantDTO.toRestaurant(id); // Convert DTO to Entity
            Restaurant savedRestaurant = restaurantRepository.save(updatedRestaurant);
            return new RestaurantDTO(savedRestaurant); // Convert back to DTO
        });
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }


}
