package com.github.io2357911.vote4lunch.to;

import java.time.LocalDate;

public class VoteTo {

    Integer id;

    Integer userId;

    int restaurantId;

    LocalDate created;

    VoteTo() {
    }

    public VoteTo(Integer id, Integer userId, int restaurantId, LocalDate created) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", userId=" + userId +
                ", restaurantId=" + restaurantId +
                ", created=" + created +
                '}';
    }
}
