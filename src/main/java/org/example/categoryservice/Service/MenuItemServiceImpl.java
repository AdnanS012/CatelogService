package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Models.MenuItem;
import org.example.categoryservice.Models.Restaurant;
import org.example.categoryservice.Repository.MenuItemRepository;
import org.example.categoryservice.Repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }


    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        MenuItem menuItem = new MenuItem(menuItemDTO.getName(), menuItemDTO.getDescription(),
                menuItemDTO.getPrice(), restaurant);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return new MenuItemDTO(savedMenuItem.getId(), savedMenuItem.getName(),
                savedMenuItem.getDescription(), savedMenuItem.getPrice(), restaurant.getId());
    }


    public List<MenuItemDTO> getAllMenuItemsForRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(MenuItemDTO::fromEntity) // Assuming a toDTO() method in MenuItem
                .collect(Collectors.toList());
    }

}
