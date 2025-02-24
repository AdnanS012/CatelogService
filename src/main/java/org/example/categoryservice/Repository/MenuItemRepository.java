package org.example.categoryservice.Repository;

import org.example.categoryservice.Models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByRestaurantId(Long restaurantId);
     Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);
}
