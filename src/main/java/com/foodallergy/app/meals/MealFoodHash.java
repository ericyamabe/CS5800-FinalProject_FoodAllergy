package com.foodallergy.app.meals;

import jakarta.persistence.*;

@Entity
@Table(name="meal_food_hash")
public class MealFoodHash {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="meal_id")
    private int mealId;

    @Column(name="food_id")
    private int foodId;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
