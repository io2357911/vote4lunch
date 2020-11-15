package com.github.io2357911.vote4lunch.restaurant;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import com.github.io2357911.vote4lunch.web.restaurant.RestaurantController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.io2357911.vote4lunch.RestaurantTestData.*;
import static org.junit.Assert.assertThrows;

@ContextConfiguration("classpath:spring/spring.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantControllerTest {

    @Autowired
    private RestaurantController controller;

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
        controller.delete(MCDONALDS_ID);
        assertThrows(NotFoundException.class, () -> controller.get(MCDONALDS_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        Restaurant entity = controller.get(MCDONALDS_ID);
        MATCHER.assertMatch(entity, mcdonalds);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    public void getAll() {
        List<Restaurant> all = controller.getAll();
        MATCHER.assertMatch(all, mcdonalds, kfc);
    }

    @Test
    public void update() {
        Restaurant updated = getUpdated();
        controller.update(updated);
        MATCHER.assertMatch(controller.get(MCDONALDS_ID), getUpdated());
    }
}