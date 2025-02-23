package org.example.categoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.categoryservice.Controller.RestaurantController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuItemsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private RestaurantController restaurantController;

    @InjectMocks
    private MenuItemsController menuItemsController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        // Given: A menu item DTO
        MenuItemDTO menuItemDTO = new MenuItemDTO(null, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), 1L);
        MenuItemDTO createdMenuItemDTO = new MenuItemDTO(1L, "Burger", "Delicious beef burger", BigDecimal.valueOf(5.99), 1L);

        given(menuItemService.createMenuItem(any(MenuItemDTO.class), eq(1L))).willReturn(createdMenuItemDTO);

        // When & Then: Perform POST request and verify response
        mockMvc.perform(post("/restaurants/1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.description").value("Delicious beef burger"))
                .andExpect(jsonPath("$.price").value(5.99))
                .andExpect(jsonPath("$.restaurantId").value(1L));
    }

}
