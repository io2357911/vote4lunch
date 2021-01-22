package com.github.io2357911.vote4lunch.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

public class DishTo extends BaseTo {

    private final int restaurantId;

    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    private final LocalDate created;

    @Range(min = 10)
    private final int price;

    @ConstructorProperties({"id", "restaurantId", "name", "created", "price"})
    public DishTo(Integer id, int restaurantId, @NotBlank String name, LocalDate created, int price) {
        super(id);
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

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", name=" + name +
                ", created=" + created +
                ", price=" + price +
                '}';
    }
}
