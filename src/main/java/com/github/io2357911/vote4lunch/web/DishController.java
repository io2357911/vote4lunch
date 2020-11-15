package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.repository.DishJpaRepository;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNew;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class DishController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final DishJpaRepository repository;
    private final RestaurantJpaRepository restaurantRepository;

    public DishController(DishJpaRepository repository, RestaurantJpaRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    public Dish create(Dish dish, int restaurantId) {
        log.info("create {}", dish);
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        return save(dish, restaurantId);
    }

    private Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }

    public void delete(int id, int restaurantId) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(int id, int restaurantId) {
        log.info("get {}", id);
        Dish dish = repository.findById(id)
                .filter(meal -> meal.getRestaurant().getId() == restaurantId)
                .orElse(null);
        return checkNotFoundWithId(dish, id);
    }

    public List<Dish> getAll(int restaurantId) {
        log.info("getAll");
        return repository.getAll(restaurantId);
    }

    public void update(Dish dish, int restaurantId) {
        log.info("update {}", dish);
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(save(dish, restaurantId), dish.id());
    }
}
