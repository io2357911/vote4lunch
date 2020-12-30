package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.RestaurantTestData;
import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.io2357911.vote4lunch.RestaurantTestData.*;
import static com.github.io2357911.vote4lunch.TestUtil.readFromJson;
import static com.github.io2357911.vote4lunch.TestUtil.userHttpBasic;
import static com.github.io2357911.vote4lunch.UserTestData.user;
import static com.github.io2357911.vote4lunch.web.RestaurantRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private RestaurantJpaRepository restaurantRepository;

    @Test
    void getRestaurants() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    void getRestaurantsWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-dishes")
                .param("date", "2020-01-30")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WITH_DISHES_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    void createRestaurant() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(restaurantRepository.findById(newId).get(), newRestaurant);
    }
}