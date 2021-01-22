package com.github.io2357911.vote4lunch.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dishes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"restaurant_id", "name", "created"}, name = "dishes_unique_restaurant_name_created_idx")})
public class Dish extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "created", nullable = false)
    @NotNull
    private LocalDate created;

    @Column(name = "price", nullable = false)
    @Range(min = 10, max = 100000)
    private int price;

    public Dish() {
    }

    public Dish(Integer id, String name, Restaurant restaurant, LocalDate created, int price) {
        super(id, name);
        this.restaurant = restaurant;
        this.created = created;
        this.price = price;
    }

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getRestaurant(), dish.getCreated(), dish.getPrice());
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name=" + name +
                ", created=" + created +
                ", price=" + price +
                '}';
    }
}
