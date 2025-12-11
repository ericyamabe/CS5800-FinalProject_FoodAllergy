package com.foodallergy.app.food;

import org.springframework.format.annotation.NumberFormat;

public class FoodAdapter implements FoodInterface {
    private Food food;

    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private double allergyProbability;

    public FoodAdapter(Food food) {
        this.food = food;
    }

    public void setName(String name) {
        this.food.setName(name);
    }

    public void setUserId(int userId) {
        this.food.setUserId(userId);
    }

    public void setAllergyProbability(double allergyProbability) {
        this.allergyProbability = allergyProbability;
    }

    public int getId() {
        return this.food.getId();
    }

    public String getName() {
        return this.food.getName();
    }

    public int getUserId() {
        return this.food.getUserId();
    }

    public double getAllergyProbability() {
        return this.allergyProbability;
    }
}
