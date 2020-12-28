package com.github.io2357911.vote4lunch.to;

import java.time.LocalDate;

public class VoteTo {

    Integer id;

    Integer userId;

    int restaurantId;

    LocalDate date;

    VoteTo() {
    }

    public VoteTo(Integer id, Integer userId, int restaurantId, LocalDate date) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", userId=" + userId +
                ", restaurantId=" + restaurantId +
                ", date=" + date +
                '}';
    }
}
