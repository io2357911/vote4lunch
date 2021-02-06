package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Dish;

import java.time.LocalDate;

import static com.github.io2357911.vote4lunch.RestaurantTestData.restaurant1;
import static com.github.io2357911.vote4lunch.RestaurantTestData.restaurant2;
import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final TestMatcher<Dish> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class,"restaurant");

    public static final int RESTAURANT1_DISH1_ID = START_SEQ + 4;
    public static final int RESTAURANT1_DISH2_ID = START_SEQ + 5;
    public static final int RESTAURANT2_DISH1_ID = START_SEQ + 6;

    public static final Dish restaurant1Dish1 = new Dish(RESTAURANT1_DISH1_ID, "Big Mac", restaurant1,
            LocalDate.now(), 199);
    public static final Dish restaurant1Dish2 = new Dish(RESTAURANT1_DISH2_ID, "Chicken Nuggets", restaurant1,
            LocalDate.of(2020, 1, 31), 99);
    public static final Dish restaurant2Dish1 = new Dish(RESTAURANT2_DISH1_ID, "Original bucket", restaurant2,
            LocalDate.now(), 79);

    public static Dish getNew() {
        return new Dish(null, "Super meal", restaurant1, LocalDate.of(2020, 2, 1), 59);
    }

    public static Dish getInvalid() {
        return new Dish(null, "", null, null, 0);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(restaurant1Dish1);
        updated.setName("Super new dish");
        updated.setCreated(LocalDate.of(2020, 2, 2));
        updated.setPrice(50);
        return updated;
    }
}
