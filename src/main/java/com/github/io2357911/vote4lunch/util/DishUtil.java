package com.github.io2357911.vote4lunch.util;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.to.DishTo;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;

public class DishUtil {
    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), null, nowIfNull(dishTo.getCreated()), dishTo.getPrice());
    }

    public static DishTo asTo(Dish entity) {
        return new DishTo(entity.getId(), entity.getRestaurant().getId(), entity.getName(), entity.getCreated(), entity.getPrice());
    }

    public static DishTo asTo(int restaurantId, Dish dish) {
        return new DishTo(dish.getId(), restaurantId, dish.getName(), dish.getCreated(), dish.getPrice());
    }
}
