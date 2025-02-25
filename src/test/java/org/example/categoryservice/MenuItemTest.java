package org.example.categoryservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.categoryservice.Models.MenuItem;
import org.example.categoryservice.Models.Restaurant;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {
    private final Validator validator;

    public MenuItemTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidMenuItemCreation() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654","tasty food");
        MenuItem menuItem = new MenuItem("Burger", "Delicious chicken burger", new BigDecimal("5.99"), restaurant);

        assertNotNull(menuItem);
        assertEquals("Burger", menuItem.getName());
        assertEquals("Delicious chicken burger", menuItem.getDescription());
        assertEquals(new BigDecimal("5.99"), menuItem.getPrice());
        assertEquals(restaurant, menuItem.getRestaurant());
    }

    @Test
    public void testMenuItemValidationFailsForInvalidData() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654","tasty food");
        MenuItem menuItem = new MenuItem("", "", BigDecimal.ZERO, restaurant);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testMenuItemValidationFailsForNegativePrice() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "tasty food");
        MenuItem menuItem = new MenuItem("Burger", "Delicious chicken burger", new BigDecimal("-1.00"), restaurant);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);

        assertFalse(violations.isEmpty());
    }
    @Test
    public void testMenuItemValidationFailsForNullRestaurant() {
        MenuItem menuItem = new MenuItem("Burger", "Delicious chicken burger", new BigDecimal("5.99"), null);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);

        assertFalse(violations.isEmpty());
    }


    @Test
    public void testMenuItemValidationFailsForEmptyNameAndDescription() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "tasty food");
        MenuItem menuItem = new MenuItem("", "", new BigDecimal("5.99"), restaurant);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testMenuItemValidationPassesForValidPrice() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "tasty food");
        MenuItem menuItem = new MenuItem("Burger", "Delicious chicken burger", new BigDecimal("0.01"), restaurant);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);

        assertTrue(violations.isEmpty());
    }
}
