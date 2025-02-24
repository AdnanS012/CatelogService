package org.example.categoryservice.Controller;

import jakarta.validation.Valid;
import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Service.MenuItemServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu-items")
public class MenuItemController {
    private final MenuItemServiceImpl menuItemServiceImpl;

    public MenuItemController(MenuItemServiceImpl menuItemServiceImpl) {
        this.menuItemServiceImpl = menuItemServiceImpl;
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@PathVariable Long restaurantId, @RequestBody @Valid MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemServiceImpl.createMenuItem(menuItemDTO, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems(@PathVariable Long restaurantId) {
        List<MenuItemDTO> menuItems = menuItemServiceImpl.getAllMenuItemsForRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }



}
