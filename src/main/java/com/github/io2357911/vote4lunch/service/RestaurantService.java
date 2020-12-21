package com.github.io2357911.vote4lunch.service;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNew;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantJpaRepository repository;

    public RestaurantService(RestaurantJpaRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        return repository.save(restaurant);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        log.info("get {}", id);
        Restaurant restaurant = repository.findById(id).orElse(null);
        return checkNotFoundWithId(restaurant, id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }

    public void update(Restaurant restaurant) {
        log.info("update {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }
}
