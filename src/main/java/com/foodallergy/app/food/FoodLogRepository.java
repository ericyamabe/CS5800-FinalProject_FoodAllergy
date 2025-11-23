package com.foodallergy.app.food;

import com.foodallergy.app.events.FoodLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface FoodLogRepository extends CrudRepository<FoodLog, Integer> {
    List<FoodLog> findByFoodId(Integer foodId);

    @Query(value = "select fl.food_id from food_log fl where fl.date_occured in (:datesToFilterBy) group by fl.food_id order by count(fl.food_id) desc", nativeQuery = true)
    List<Object> getFoodMostCommonByDates(@Param("datesToFilterBy") ArrayList<LocalDate> datesToFilterBy);

    @Query(value = "select food_id from food_log where user_id = :idToFilterBy group by food_id order by count(food_id) desc", nativeQuery = true)
    List<Object> getFoodMostCommonByUserId(@Param("idToFilterBy") Integer idToFilterBy);
}
