package com.foodallergy.app.food;

import com.foodallergy.app.events.FoodLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodLogRepository extends CrudRepository<FoodLog, Integer> {
    List<FoodLog> findByFoodId(Integer foodId);
}
