package com.foodallergy.app.food;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodsHelper {
    private static FoodsHelper instance;

    private FoodsHelper() {
    }

    public static FoodsHelper getInstance() {
        if (instance == null) {
            instance = new FoodsHelper();
        }
        return instance;
    }

    public ArrayList<Food> getMostCommonByUserId(
            FoodLogRepository foodLogRepository,
            FoodRepository foodRepository,
            int userId) {
        ArrayList<Food> foods = new ArrayList<Food>();

        List<Object> foodsList = foodLogRepository.getFoodMostCommonByUserId(userId);

        for (Object foodLog : foodsList) {
            Food food = foodRepository.findById(Integer.parseInt(foodLog.toString()));
            foods.add(food);
        }

        return foods;
    }

    public ArrayList<Food> getFoodMostCommonByDates(
            FoodLogRepository foodLogRepository,
            FoodRepository foodRepository,
            ArrayList<LocalDate> dates) {
        ArrayList<Food> foods = new ArrayList<Food>();
        List<Object> foodIds = foodLogRepository.getFoodMostCommonByDates(dates);

        for (Object food : foodIds) {
            Food f = foodRepository.findById(Integer.parseInt(food.toString()));
            foods.add(f);
        }

        return foods;
    }
}
