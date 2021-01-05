package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.repository.DishJpaRepository;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.to.DishTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController extends AbstractRestController {
    static final String REST_URL = "/rest/dishes";

    private final DishJpaRepository dishRepository;
    private final RestaurantJpaRepository restaurantRepository;

    public DishRestController(DishJpaRepository dishRepository, RestaurantJpaRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<Dish> getDishes(@RequestParam int restaurantId,
                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll restaurantId={}, date={}", restaurantId, date);
        return dishRepository.getAll(restaurantId, nowIfNull(date));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = {CACHE_RESTAURANTS_WITH_DISHES}, allEntries = true)
    public ResponseEntity<Dish> createDish(@Valid @RequestBody DishTo dishTo) {
        log.info("create {}", dishTo);

        Dish newDish = fromTo(dishTo);
        newDish.setRestaurant(restaurantRepository.getOne(dishTo.getRestaurantId()));
        Dish created = dishRepository.save(newDish);
        return createResponseEntity(REST_URL, created.getId(), created);
    }

    private static Dish fromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), nowIfNull(dishTo.getDate()), dishTo.getPrice());
    }
}
