package org.example.categoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.categoryservice.Controller.MenuItemController;
import org.example.categoryservice.DTO.MenuItemDTO;
import org.example.categoryservice.Service.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuItemsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MenuItemServiceImpl menuItemServiceImpl;



    @InjectMocks
    private MenuItemController menuItemsController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemsController).build();
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        // Given: A menu item DTO
        MenuItemDTO menuItemDTO = new MenuItemDTO(null, "Burger", "Delicious cheeseburger", BigDecimal.valueOf(5.99), 1L);

        // The expected response (simulating the saved entity)
        MenuItemDTO createdMenuItem = new MenuItemDTO(1L, "Burger", "Delicious cheeseburger", BigDecimal.valueOf(5.99), 1L);

        // Mock the service call
        BDDMockito.given(menuItemServiceImpl.createMenuItem(ArgumentMatchers.any(MenuItemDTO.class), eq(1L))).willReturn(createdMenuItem);

        // When & Then: Mock the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/menu-items") // Ensure restaurant ID is passed
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO))) // Convert DTO to JSON
                .andExpect(status().isCreated()) // HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.description").value("Delicious cheeseburger"))
                .andExpect(jsonPath("$.price").value(5.99))
                .andExpect(jsonPath("$.restaurantId").value(1));
    }

    @Test
    public void testGetAllMenuItemsForRestaurant() throws Exception {
        // Given: A restaurant with ID 1 and some menu items
        Long restaurantId = 1L;
        List<MenuItemDTO> menuItems = List.of(
                new MenuItemDTO(1L, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId),
                new MenuItemDTO(2L, "Pizza", "Cheesy pepperoni pizza", BigDecimal.valueOf(8.99), restaurantId)
        );

        BDDMockito.given(menuItemServiceImpl.getAllMenuItemsForRestaurant(restaurantId)).willReturn(menuItems);

        // When & Then: Mock the request and verify response
        mockMvc.perform(get("/restaurants/{restaurantId}/menu-items", restaurantId))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Burger"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Pizza"));
    }

   @Test
   public void testGetMenuItemOfARestaurant() throws Exception {
       Long restaurantId = 1L;
       Long menuitemId = 10L;
       MenuItemDTO menuItemDTO = new MenuItemDTO(menuitemId, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId);

       BDDMockito.given(menuItemServiceImpl.getMenuItemById(restaurantId, menuitemId)).willReturn(Optional.of(menuItemDTO));

       mockMvc.perform(get("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuitemId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(menuitemId))
               .andExpect(jsonPath("$.name").value("Burger"))
               .andExpect(jsonPath("$.description").value("Delicious beef burger"))
               .andExpect(jsonPath("$.price").value(5.99));
   }
    @Test
    public void testGetMenuItemForNonExistentRestaurant() throws Exception {
        Long restaurantId = 999L;
        Long menuItemId = 1L;

        BDDMockito.given(menuItemServiceImpl.getMenuItemById(restaurantId, menuItemId))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuItemId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMenuItemOfRestaurant() throws Exception {
        Long restaurantId = 1L;
        MenuItemDTO menuItemDTO = new MenuItemDTO(null, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId);
        MenuItemDTO createdMenuItemDTO = new MenuItemDTO(1L, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId);

        BDDMockito.given(menuItemServiceImpl.createMenuItem(ArgumentMatchers.any(MenuItemDTO.class), eq(restaurantId))).willReturn(createdMenuItemDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/{restaurantId}/menu-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuItemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.description").value("Delicious beef burger"))
                .andExpect(jsonPath("$.price").value(5.99))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
    }
    @Test
    public void testCreateMenuItemWithInvalidData() throws Exception {
        Long restaurantId = 1L;
        MenuItemDTO invalidMenuItemDTO = new MenuItemDTO(null, "", "Invalid item", BigDecimal.valueOf(-1), restaurantId);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/{restaurantId}/menu-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidMenuItemDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateMenuItemForNonExistentRestaurant() throws Exception {
        Long restaurantId = 999L;
        MenuItemDTO menuItemDTO = new MenuItemDTO(null, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId);

        BDDMockito.given(menuItemServiceImpl.createMenuItem(ArgumentMatchers.any(MenuItemDTO.class), eq(restaurantId)))
                .willThrow(new IllegalArgumentException("Restaurant not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/{restaurantId}/menu-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        Long restaurantId = 1L;
        Long menuItemId = 10L;
        MenuItemDTO menuItemDTO = new MenuItemDTO(menuItemId, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), restaurantId);
        MenuItemDTO updatedMenuItemDTO = new MenuItemDTO(menuItemId, "Burger", "Updated beef burger", BigDecimal.valueOf(6.99), restaurantId);

        BDDMockito.given(menuItemServiceImpl.updateMenuItem(eq(restaurantId), eq(menuItemId), ArgumentMatchers.any(MenuItemDTO.class)))
                .willReturn(updatedMenuItemDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(menuItemId))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.description").value("Updated beef burger"))
                .andExpect(jsonPath("$.price").value(6.99))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
    }

    @Test
    public void testDeleteMenuItemFromRestaurant() throws Exception {
        Long restaurantId = 1L;
        Long menuItemId = 10L;
        doNothing().when(menuItemServiceImpl).deleteMenuItem(restaurantId, menuItemId);

        mockMvc.perform(delete("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuItemId))
                .andExpect(status().isNoContent()); //Expect 204 no content

    }

    @Test
    public void testDeleteNonExistentMenuItem() throws Exception {
        Long restaurantId = 1L;
        Long menuItemId = 999L; // Non-existent menu item

        doThrow(new ResourceNotFoundException("Menu item not found"))
                .when(menuItemServiceImpl).deleteMenuItem(restaurantId, menuItemId);

        mockMvc.perform(delete("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuItemId))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found
    }
    @Test
    public void testDeleteMenuItemForNonExistentRestaurant() throws Exception {
        Long restaurantId = 999L;
        Long menuItemId = 1L;

        doThrow(new ResourceNotFoundException("Restaurant not found"))
                .when(menuItemServiceImpl).deleteMenuItem(restaurantId, menuItemId);

        mockMvc.perform(delete("/restaurants/{restaurantId}/menu-items/{menuItemId}", restaurantId, menuItemId))
                .andExpect(status().isNotFound());
    }





}
