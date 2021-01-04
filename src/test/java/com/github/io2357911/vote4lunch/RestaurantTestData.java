package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Restaurant;

import java.util.Collections;

import static com.github.io2357911.vote4lunch.DishTestData.restaurant1Dish1;
import static com.github.io2357911.vote4lunch.DishTestData.restaurant2Dish1;
import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "dishes", "votes");

    public static TestMatcher<Restaurant> WITH_DISHES_MATCHER =
            TestMatcher.usingAssertions(Restaurant.class,
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    },
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant").isEqualTo(e)
            );

    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 2;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "McDonald's");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "KFC");

    static {
        restaurant1.setDishes(Collections.singletonList(restaurant1Dish1));
        restaurant2.setDishes(Collections.singletonList(restaurant2Dish1));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "GoodFood");
    }

    public static Restaurant getInvalid() {
        return new Restaurant(null, "");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName("NewFood");
        return updated;
    }
}
