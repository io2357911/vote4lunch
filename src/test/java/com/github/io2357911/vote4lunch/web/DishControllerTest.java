package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import org.junit.Test;

import java.util.List;

import static com.github.io2357911.vote4lunch.DishTestData.*;
import static com.github.io2357911.vote4lunch.RestaurantTestData.RESTAURANT2_ID;
import static com.github.io2357911.vote4lunch.RestaurantTestData.RESTAURANT1_ID;
import static org.junit.Assert.assertThrows;

public class DishControllerTest extends AbstractControllerTest<DishController> {

    @Test
    public void create() {
        Dish created = controller.create(getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Dish newEntity = getNew();
        newEntity.setId(newId);
        MATCHER.assertMatch(created, newEntity);
        MATCHER.assertMatch(controller.get(newId, RESTAURANT1_ID), newEntity);
    }

    @Test
    public void delete() {
        controller.delete(MCDONALDS_DISH1_ID, RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.get(MCDONALDS_DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    public void get() {
        Dish entity = controller.get(MCDONALDS_DISH1_ID, RESTAURANT1_ID);
        MATCHER.assertMatch(entity, mcdonaldsDish1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    public void getAll() {
        List<Dish> all = controller.getAll(RESTAURANT1_ID);
        MATCHER.assertMatch(all, mcdonaldsDish1, mcdonaldsDish2);
    }

    @Test
    public void update() {
        Dish updated = getUpdated();
        controller.update(updated, RESTAURANT1_ID);
        MATCHER.assertMatch(controller.get(MCDONALDS_DISH1_ID, RESTAURANT1_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> controller.update(mcdonaldsDish1, RESTAURANT2_ID));
        MATCHER.assertMatch(controller.get(MCDONALDS_DISH1_ID, RESTAURANT1_ID), mcdonaldsDish1);
    }
}