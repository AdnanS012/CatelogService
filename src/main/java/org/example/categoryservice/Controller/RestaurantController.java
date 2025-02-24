package org.example.categoryservice.Controller;

import jakarta.validation.Valid;
import org.example.categoryservice.DTO.RestaurantDTO;
import org.example.categoryservice.Service.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantServiceImpl restaurantServiceImpl;

    public RestaurantController(RestaurantServiceImpl restaurantServiceImpl) {
        this.restaurantServiceImpl = restaurantServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurantList = restaurantServiceImpl.getAllRestaurants();
        return ResponseEntity.ok(restaurantList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        Optional<RestaurantDTO> restaurantDTO = restaurantServiceImpl.getRestaurantById(id);
        return restaurantDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {

        RestaurantDTO createdRestaurant = restaurantServiceImpl.createRestaurant(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDTO restaurantDTO) {
        Optional<RestaurantDTO> updatedRestaurant = restaurantServiceImpl.updateRestaurant(id, restaurantDTO);
        return updatedRestaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantServiceImpl.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

