package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import org.junit.Test;

import java.util.List;

import static com.github.io2357911.vote4lunch.DishTestData.*;
import static com.github.io2357911.vote4lunch.RestaurantTestData.KFC_ID;
import static com.github.io2357911.vote4lunch.RestaurantTestData.MCDONALDS_ID;
import static org.junit.Assert.assertThrows;

public class DishControllerTest extends AbstractControllerTest<DishController> {

    @Test
    public void create() {
        Dish created = controller.create(getNew(), MCDONALDS_ID);
        int newId = created.id();
        Dish newEntity = getNew();
        newEntity.setId(newId);
        MATCHER.assertMatch(created, newEntity);
        MATCHER.assertMatch(controller.get(newId, MCDONALDS_ID), newEntity);
    }

    @Test
    public void delete() {
        controller.delete(MCDONALDS_DISH1_ID, MCDONALDS_ID);
        assertThrows(NotFoundException.class, () -> controller.get(MCDONALDS_DISH1_ID, MCDONALDS_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND, MCDONALDS_ID));
    }

    @Test
    public void get() {
        Dish entity = controller.get(MCDONALDS_DISH1_ID, MCDONALDS_ID);
        MATCHER.assertMatch(entity, mcdonaldsDish1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND, MCDONALDS_ID));
    }

    @Test
    public void getAll() {
        List<Dish> all = controller.getAll(MCDONALDS_ID);
        MATCHER.assertMatch(all, mcdonaldsDish1, mcdonaldsDish2);
    }

    @Test
    public void update() {
        Dish updated = getUpdated();
        controller.update(updated, MCDONALDS_ID);
        MATCHER.assertMatch(controller.get(MCDONALDS_DISH1_ID, MCDONALDS_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> controller.update(mcdonaldsDish1, KFC_ID));
        MATCHER.assertMatch(controller.get(MCDONALDS_DISH1_ID, MCDONALDS_ID), mcdonaldsDish1);
    }
}