package com.foodallergy.app.meals;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MealFoodHashRepository extends CrudRepository<MealFoodHash, Long> {
    List<MealFoodHash> findByMealId(int mealId);
}
