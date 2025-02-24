package org.example.categoryservice.Service;

import org.example.categoryservice.DTO.MenuItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    List<MenuItemDTO> getAllMenuItemsForRestaurant(Long restaurantId);

}
