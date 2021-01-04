package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController extends AbstractRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantJpaRepository repository;

    public RestaurantRestController(RestaurantJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Restaurant> getRestaurants() {
        log.info("getAll");
        return repository.findAll();
    }

    @GetMapping("/with-dishes")
    public List<Restaurant> getRestaurantsWithDishes(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWithDishes date={}", date);
        return repository.getWithDishes(nowIfNull(date));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        return createResponseEntity(REST_URL, created.getId(), created);
    }
}
