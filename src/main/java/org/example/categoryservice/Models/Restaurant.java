package org.example.categoryservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;


@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Location cannot be empty")
    private String location;
    @NotEmpty(message = "Cuisine cannot be empty")
    private String cuisine;
    @NotEmpty(message = "Contact cannot be empty")
    private String contact;
    private String description;


    protected Restaurant(){}

    public Restaurant(Long id,String name, String location , String cuisine,String contact,String description) {
        this.id=id;
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.contact = contact;
        this.description = description;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCuisine() { return cuisine; }
    public String getContact() { return contact; }
    public String getDescription() { return description; }



}
