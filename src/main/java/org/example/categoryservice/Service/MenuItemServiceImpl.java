package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Models.MenuItem;
import org.example.categoryservice.Models.Restaurant;
import org.example.categoryservice.Repository.MenuItemRepository;
import org.example.categoryservice.Repository.RestaurantRepository;
import org.example.categoryservice.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<MenuItemDTO> getMenuItemById(Long restaurantId, Long menuItemId) {

        return menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .map(menuItem -> new MenuItemDTO(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getRestaurant().getId()));

    }

    @Override
    public MenuItemDTO updateMenuItem(Long restaurantId, Long menuItemId, MenuItemDTO menuItemDTO) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Menu item does not belong to the specified restaurant");
        }

     menuItem.updateDetails(menuItemDTO.getName(), menuItemDTO.getDescription(), menuItemDTO.getPrice());

        menuItemRepository.save(menuItem);

        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getRestaurant().getId()
        );
    }

    @Override
    public void deleteMenuItem(Long restaurantId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("Menu item does not belong to the specified restaurant");
        }

        menuItemRepository.delete(menuItem);
    }


}
