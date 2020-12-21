package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Dish;

import java.time.LocalDate;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class,"restaurant");

    public static final int MCDONALDS_DISH1_ID = START_SEQ + 4;
    public static final int MCDONALDS_DISH2_ID = START_SEQ + 5;
    public static final int KFC_DISH1_ID = START_SEQ + 6;
    public static final int NOT_FOUND = 2;

    public static final Dish mcdonaldsDish1 = new Dish(MCDONALDS_DISH1_ID, "Big Mac",
            LocalDate.of(2020, 1, 30), 199);
    public static final Dish mcdonaldsDish2 = new Dish(MCDONALDS_DISH2_ID, "Chicken Nuggets",
            LocalDate.of(2020, 1, 31), 99);
    public static final Dish kfcDish1 = new Dish(KFC_DISH1_ID, "Original bucket",
            LocalDate.of(2020, 1, 30), 79);

    public static Dish getNew() {
        return new Dish(null, "Super meal", LocalDate.of(2020, 2, 1), 59);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(mcdonaldsDish1);
        updated.setName("Super new meal");
        updated.setDate(LocalDate.of(2020, 2, 2));
        updated.setPrice(1);
        return updated;
    }
}
