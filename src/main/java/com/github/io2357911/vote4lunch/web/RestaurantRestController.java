package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.repository.RestaurantRepository;
import com.github.io2357911.vote4lunch.to.RestaurantTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.RestaurantUtil.*;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController extends AbstractRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantRepository repository;

    public RestaurantRestController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Cacheable(CACHE_RESTAURANTS)
    public List<Restaurant> getRestaurants() {
        log.info("getAll");
        return repository.findAll();
    }

    @GetMapping("/with-dishes")
    @Cacheable(value = CACHE_RESTAURANTS_WITH_DISHES, key = "#date")
    public List<Restaurant> getRestaurantsWithDishes(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            log.info("getWithDishes date={}", date);
        return repository.getWithDishes(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = {CACHE_RESTAURANTS, CACHE_RESTAURANTS_WITH_DISHES}, allEntries = true)
    public ResponseEntity<RestaurantTo> createRestaurant(@Valid @RequestBody RestaurantTo to) {
        log.info("create {}", to);
        checkNew(to);
        RestaurantTo created = asTo(repository.save(createNewFromTo(to)));
        return createResponseEntity(REST_URL, created.getId(), created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateRestaurant(@Valid @RequestBody RestaurantTo to, @PathVariable int id) {
        log.info("update {}", to);
        assureIdConsistent(to, id);
        checkNotFoundWithId(repository.findById(id), "Restaurant id=" + id + " not found");
        repository.save(asEntity(to));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable int id) {
        log.info("delete {}", id);
        checkSingleModification(repository.delete(id), "Restaurant id=" + id + " not found");
    }
}
