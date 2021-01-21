package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishJpaRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.created=:created")
    List<Dish> getAll(@Param("restaurantId") int restaurantId, @Param("created") LocalDate created);
}
