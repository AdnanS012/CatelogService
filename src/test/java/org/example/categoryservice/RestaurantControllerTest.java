package org.example.categoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.categoryservice.Controller.RestaurantController;
import org.example.categoryservice.DTO.RestaurantDTO;
import org.example.categoryservice.Service.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RestaurantControllerTest {

    private MockMvc mockMvc;
    @Mock
    private RestaurantServiceImpl restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testGetAllRestaurants() throws Exception {
        List<RestaurantDTO> restaurantList = new ArrayList<>();
        when(restaurantService.getAllRestaurants()).thenReturn(restaurantList);

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    public void testGetRestaurantById() throws Exception {
        // Given: A restaurant with ID 1
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "KFC", "New York", "Fast Food", "+124567654", "Description");
        given(restaurantService.getRestaurantById(1L)).willReturn(Optional.of(restaurantDTO));

        // When & Then: Mock the request and verify response
        mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("KFC"))
                .andExpect(jsonPath("$.location").value("New York"))
                .andExpect(jsonPath("$.cuisine").value("Fast Food"))
                .andExpect(jsonPath("$.contact").value("+124567654"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    public void testCreateRestaurant() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(null, "KFC", "New York", "Fast Food", "+124567654", "Description");
        RestaurantDTO createdRestaurant = new RestaurantDTO(1L, "KFC", "New York", "Fast Food", "+124567654", "Description");
        given(restaurantService.createRestaurant(any(RestaurantDTO.class))).willReturn(createdRestaurant);
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurantDTO))) // Convert DTO to JSON
                .andExpect(status().isCreated()) // HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("KFC"))
                .andExpect(jsonPath("$.location").value("New York"))
                .andExpect(jsonPath("$.cuisine").value("Fast Food"))
                .andExpect(jsonPath("$.contact").value("+124567654"))
                .andExpect(jsonPath("$.description").value("Description"));
    }
    @Test
    public void testCreateRestaurantInvalidInput() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(null, "", "New York", "Fast Food", "+124567654", "Description");

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurantDTO)))
                .andExpect(status().isBadRequest());// HTTP 400

    }
    @Test
    public void testUpdateRestaurant() throws Exception {
        // Given: An existing restaurant and updated details
        RestaurantDTO updatedRestaurant = new RestaurantDTO(1L, "McDonald's", "Los Angeles", "Fast Food", "+123456789", "Updated Description");

        given(restaurantService.updateRestaurant(eq(1L), any(RestaurantDTO.class)))
                .willReturn(Optional.of(updatedRestaurant));

        // When & Then: Mock the request and verify response
        mockMvc.perform(put("/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedRestaurant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("McDonald's"))
                .andExpect(jsonPath("$.location").value("Los Angeles"))
                .andExpect(jsonPath("$.cuisine").value("Fast Food"))
                .andExpect(jsonPath("$.contact").value("+123456789"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }


    @Test
    public void testUpdateRestaurant1() throws Exception {
        // Given: A restaurant exists in the system
        Long restaurantId = 1L;
        RestaurantDTO updatedDTO = new RestaurantDTO(null, "Updated KFC", "Los Angeles", "Fast Food", "+1987654321", "New Description");
        RestaurantDTO updatedResponseDTO = new RestaurantDTO(restaurantId, "Updated KFC", "Los Angeles", "Fast Food", "+1987654321", "New Description");

        // Mock service behavior
        given(restaurantService.updateRestaurant(eq(restaurantId), any(RestaurantDTO.class)))
                .willReturn(Optional.of(updatedResponseDTO));

        // When & Then: Perform PUT request and verify response
        mockMvc.perform(put("/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDTO)))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.id").value(restaurantId))
                .andExpect(jsonPath("$.name").value("Updated KFC"))
                .andExpect(jsonPath("$.location").value("Los Angeles"))
                .andExpect(jsonPath("$.cuisine").value("Fast Food"))
                .andExpect(jsonPath("$.contact").value("+1987654321"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    public void testDeleteRestaurant() throws Exception {
        Long restaurantId = 1L;
        doNothing().when(restaurantService).deleteRestaurant(restaurantId);

        // When & Then: Mock the request and verify response
        mockMvc.perform(delete("/restaurants/{id}", restaurantId))
                .andExpect(status().isNoContent()); // HTTP 204

    }


}

