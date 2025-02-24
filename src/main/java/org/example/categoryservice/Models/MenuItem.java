package org.example.categoryservice.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id",nullable = false)
    private Restaurant restaurant;

    protected MenuItem() {}

    public MenuItem(String name, String description, BigDecimal price, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Restaurant getRestaurant() { return restaurant; }

public void updateDetails(String name,String description,BigDecimal price){
        this.name=name;
        this.description=description;
        this.price=price;
}


}
