package org.example.categoryservice.Controller;

import jakarta.validation.Valid;
import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Service.MenuItemServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/restaurants/{restaurantId}/menu-items")
public class MenuItemController {
    private final MenuItemServiceImpl menuItemServiceImpl;

    public MenuItemController(MenuItemServiceImpl menuItemServiceImpl) {
        this.menuItemServiceImpl = menuItemServiceImpl;
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @PathVariable Long restaurantId, @RequestBody @Valid MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemServiceImpl.createMenuItem(menuItemDTO, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems(@PathVariable Long restaurantId) {
        List<MenuItemDTO> menuItems = menuItemServiceImpl.getAllMenuItemsForRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long restaurantId,@PathVariable Long menuItemId){
        return menuItemServiceImpl.getMenuItemById(restaurantId,menuItemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long restaurantId, @PathVariable Long menuItemId, @RequestBody @Valid MenuItemDTO menuItemDTO) {
     MenuItemDTO updateMenuItem = menuItemServiceImpl.updateMenuItem(restaurantId, menuItemId, menuItemDTO);
        return ResponseEntity.ok(updateMenuItem);
    }

}
