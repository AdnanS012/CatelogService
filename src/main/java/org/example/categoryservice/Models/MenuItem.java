package org.example.categoryservice.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    @ManyToMany
    @JoinColumn(name = "restaurant_id",nullable = false)
    private Restaurant restaurant;

    protected MenuItem() {}

    public MenuItem(Long id,String name, String description, BigDecimal price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }
}
