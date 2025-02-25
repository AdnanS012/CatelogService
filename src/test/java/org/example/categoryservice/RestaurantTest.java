package org.example.categoryservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.categoryservice.Models.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {
    private final Validator validator;

    public RestaurantTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidRestaurantCreation() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "Tasty meals");

        assertNotNull(restaurant);
        assertEquals("KFC", restaurant.getName());
        assertEquals("New York", restaurant.getLocation());
        assertEquals("Fast Food", restaurant.getCuisine());
        assertEquals("+124567654", restaurant.getContact());
        assertEquals("Tasty meals", restaurant.getDescription());
    }

    @Test
    public void testRestaurantValidationFailsForInvalidData() {
        Restaurant restaurant = new Restaurant(null, "", "", "invalid", "","");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
    }
    @Test
    public void testRestaurantValidationFailsForNullName() {
        Restaurant restaurant = new Restaurant(1L, null, "New York", "Fast Food", "+124567654", "Tasty meals");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testRestaurantValidationFailsForNullLocation() {
        Restaurant restaurant = new Restaurant(1L, "KFC", null, "Fast Food", "+124567654", "Tasty meals");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testRestaurantValidationFailsForNullCuisine() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", null, "+124567654", "Tasty meals");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
    }
    @Test
    public void testRestaurantValidationFailsForNullContact() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", null, "Tasty meals");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testRestaurantValidationPassesForEmptyDescription() {
        Restaurant restaurant = new Restaurant(1L, "KFC", "New York", "Fast Food", "+124567654", "");

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertTrue(violations.isEmpty());
    }

}
