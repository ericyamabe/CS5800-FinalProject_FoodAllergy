package com.foodallergy.app.events;

import jakarta.persistence.*;

@Entity
@Table(name="food_log")
public class FoodLog {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="food_id")
    private int foodId;

    @Column(name="user_id")
    private int userId;

    @Column(name="date_occured")
    private String dateOccured;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateOccured() {
        return dateOccured;
    }

    public void setDateOccured(String dateOccured) {
        this.dateOccured = dateOccured;
    }
}
