package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.repository.DishJpaRepository;
import com.github.io2357911.vote4lunch.to.DishTo;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import com.github.io2357911.vote4lunch.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static com.github.io2357911.vote4lunch.DishTestData.*;
import static com.github.io2357911.vote4lunch.RestaurantTestData.NOT_FOUND;
import static com.github.io2357911.vote4lunch.RestaurantTestData.restaurant1;
import static com.github.io2357911.vote4lunch.TestUtil.readFromJson;
import static com.github.io2357911.vote4lunch.TestUtil.userHttpBasic;
import static com.github.io2357911.vote4lunch.UserTestData.admin;
import static com.github.io2357911.vote4lunch.UserTestData.user;
import static com.github.io2357911.vote4lunch.util.DishUtil.asTo;
import static com.github.io2357911.vote4lunch.web.DishRestController.REST_URL;
import static com.github.io2357911.vote4lunch.web.ExceptionInfoHandler.EXCEPTION_DISH_DUPLICATE;
import static com.github.io2357911.vote4lunch.web.ExceptionInfoHandler.EXCEPTION_DISH_FK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private DishJpaRepository dishRepository;

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT1_DISH1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1Dish1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + RESTAURANT1_DISH1_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(dishRepository.findById(RESTAURANT1_DISH1_ID).get(), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + RESTAURANT1_DISH1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishRepository.getExisted(RESTAURANT1_DISH1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.getId()))
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(Collections.singletonList(restaurant1Dish1)));
    }

    @Test
    void create() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(restaurant1.getId(), newDish))))
                .andDo(print());

        Dish created = readFromJson(action, Dish.class);
        newDish.setId(created.id());
        MATCHER.assertMatch(created, newDish);
        MATCHER.assertMatch(dishRepository.findById(newDish.id()).get(), newDish);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(restaurant1.getId(), getInvalid()))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorInfo(REST_URL, UNPROCESSABLE_ENTITY, "[price] must be between 10 and 9223372036854775807",
                        "[name] size must be between 2 and 100", "[name] must not be blank"));
    }

    @Test
    void createInvalidRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(NOT_FOUND, getNew()))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorInfo(REST_URL, CONFLICT, EXCEPTION_DISH_FK_NOT_FOUND));
    }

    @Test
    void createDuplicate() throws Exception {
        DishTo to = asTo(restaurant1Dish1);
        to.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorInfo(REST_URL, CONFLICT, EXCEPTION_DISH_DUPLICATE));
    }

    @Test
    void createAccessDenied() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(restaurant1.getId(), getNew()))))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(errorInfo(REST_URL, FORBIDDEN, "Access is denied"));
    }
}