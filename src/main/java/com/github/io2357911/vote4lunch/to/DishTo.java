package com.github.io2357911.vote4lunch.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

public class DishTo {

    int restaurantId;

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    LocalDate created;

    @Range(min = 10, max = 100000)
    int price;

    @ConstructorProperties({"restaurantId", "name", "created", "price"})
    public DishTo(int restaurantId, @NotBlank String name, LocalDate created, @Range(max = 100000) int price) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.created = created;
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreated() {
        return created;
    }

    public int getPrice() {
        return price;
    }
}
