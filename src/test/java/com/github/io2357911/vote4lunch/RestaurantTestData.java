package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Restaurant;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> MATCHER = TestMatcher.usingIgnoringFieldsComparator("dishes");

    public static final int MCDONALDS_ID = START_SEQ + 2;
    public static final int KFC_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 2;

    public static final Restaurant mcdonalds = new Restaurant(MCDONALDS_ID, "McDonald's");
    public static final Restaurant kfc = new Restaurant(KFC_ID, "KFC");

    public static Restaurant getNew() {
        return new Restaurant(null, "GoodFood");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(mcdonalds);
        updated.setName("NewFood");
        return updated;
    }
}
