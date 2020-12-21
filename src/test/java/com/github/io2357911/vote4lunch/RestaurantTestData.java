package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Restaurant;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class,"dishes", "votes");

    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 2;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "McDonald's");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "KFC");

    public static Restaurant getNew() {
        return new Restaurant(null, "GoodFood");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName("NewFood");
        return updated;
    }
}
