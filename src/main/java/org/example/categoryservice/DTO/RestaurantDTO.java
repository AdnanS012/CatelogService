package org.example.categoryservice.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.categoryservice.Models.Restaurant;

public class RestaurantDTO {
    private  Long id;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private  String name;
    @NotBlank(message = "Location cannot be empty")
    private  String location;
    @NotBlank(message = "Cuisine cannot be empty")
    private  String cuisine;
    @NotBlank(message = "Contact cannot be empty")
    private  String contact;
    @NotBlank(message = "Description cannot be empty")
    private  String description;

    public RestaurantDTO() {}

    public RestaurantDTO(Long id, String name, String location, String cuisine, String contact, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.contact = contact;
        this.description = description;
    }

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.location = restaurant.getLocation();
        this.cuisine = restaurant.getCuisine();
        this.contact = restaurant.getContact();
        this.description = restaurant.getDescription();
    }

    // Factory method to convert DTO to Entity (for updates)
    public Restaurant toRestaurant(Long existingId) {
        return new Restaurant(existingId, this.name, this.location, this.cuisine, this.contact, this.description);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCuisine() { return cuisine; }
    public String getContact() { return contact; }
    public String getDescription() { return description; }


}
