package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.repository.DishRepository;
import com.github.io2357911.vote4lunch.repository.RestaurantRepository;
import com.github.io2357911.vote4lunch.to.DishTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.DishUtil.createNewFromTo;
import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.*;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController extends AbstractRestController {
    static final String REST_URL = "/rest/dishes";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishRestController(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<Dish> getDishes(@RequestParam int restaurantId,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate created) {
        log.info("getAll restaurantId={}, created={}", restaurantId, created);
        return dishRepository.getAll(restaurantId, nowIfNull(created));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = {CACHE_RESTAURANTS_WITH_DISHES}, allEntries = true)
    public ResponseEntity<Dish> createDish(@Valid @RequestBody DishTo to) {
        log.info("create {}", to);
        checkNew(to);
        var newDish = createNewFromTo(to);
        newDish.setRestaurant(restaurantRepository.getOne(to.getRestaurantId()));
        var created = dishRepository.save(newDish);
        return createResponseEntity(REST_URL, created.getId(), created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDish(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateDish(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("update {}", to);
        assureIdConsistent(to, id);
        checkNotFoundWithId(dishRepository.findById(id), "Dish id=" + id + " not found");
        var newDish = createNewFromTo(to);
        newDish.setId(to.getId());
        newDish.setRestaurant(restaurantRepository.getOne(to.getRestaurantId()));
        dishRepository.save(newDish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable int id) {
        log.info("delete {}", id);
        checkSingleModification(dishRepository.delete(id), "Dish id=" + id + " not found");
    }
}
