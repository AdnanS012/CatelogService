package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.MenuItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MenuService {
    List<MenuItemDTO> getAllMenuItemsForRestaurant(Long restaurantId);
   Optional<MenuItemDTO> getMenuItemById(Long restaurantId, Long menuItemId);
    MenuItemDTO updateMenuItem(Long restaurantId, Long menuItemId, MenuItemDTO menuItemDTO);
}
