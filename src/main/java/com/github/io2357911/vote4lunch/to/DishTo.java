package com.github.io2357911.vote4lunch.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

public class DishTo {

    int restaurantId;

    @NotBlank
    String name;

    LocalDate date;

    @Range(max = 100000)
    int price;

    @ConstructorProperties({"restaurantId", "name", "date", "price"})
    public DishTo(int restaurantId, @NotBlank String name, LocalDate date, @Range(max = 100000) int price) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
