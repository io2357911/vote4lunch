package com.github.io2357911.vote4lunch.util;

import com.github.io2357911.vote4lunch.model.Restaurant;
import com.github.io2357911.vote4lunch.to.RestaurantTo;

public class RestaurantUtil {
    public static Restaurant createNewFromTo(RestaurantTo to) {
        return new Restaurant(null, to.getName());
    }

    public static RestaurantTo asTo(Restaurant entity) {
        return new RestaurantTo(entity.getId(), entity.getName());
    }

    public static Restaurant asEntity(RestaurantTo to) {
        return new Restaurant(to.getId(), to.getName());
    }
}
