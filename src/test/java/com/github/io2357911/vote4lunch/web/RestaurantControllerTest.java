package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import org.junit.Test;

import java.util.List;

import static com.github.io2357911.vote4lunch.RestaurantTestData.*;
import static org.junit.Assert.assertThrows;

public class RestaurantControllerTest extends AbstractControllerTest<RestaurantController> {

    @Test
    public void create() {
        Restaurant created = controller.create(getNew());
        int newId = created.id();
        Restaurant newEntity = getNew();
        newEntity.setId(newId);
        MATCHER.assertMatch(created, newEntity);
        MATCHER.assertMatch(controller.get(newId), newEntity);
    }

    @Test
    public void delete() {
        controller.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.get(RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        Restaurant entity = controller.get(RESTAURANT1_ID);
        MATCHER.assertMatch(entity, restaurant1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    public void getAll() {
        List<Restaurant> all = controller.getAll();
        MATCHER.assertMatch(all, restaurant1, restaurant2);
    }

    @Test
    public void update() {
        Restaurant updated = getUpdated();
        controller.update(updated);
        MATCHER.assertMatch(controller.get(RESTAURANT1_ID), getUpdated());
    }
}