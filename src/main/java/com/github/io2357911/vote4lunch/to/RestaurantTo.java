package com.github.io2357911.vote4lunch.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;

public class RestaurantTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @ConstructorProperties({"id", "name"})
    public RestaurantTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
