package com.foodallergy.app.food;

public interface FoodInterface {
    public void setName(String name);
    public void setUserId(int userId);
    public int getId();
    public String getName();
    public int getUserId();
    public double getAllergyProbability();
    public void setAllergyProbability(double allergyProbability);
}
