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

    @Query(value = "select fl.food_id from food_log fl where fl.date_occured in (:datesToFilterBy) and user_id = :idToFilterBy group by fl.food_id order by count(fl.food_id) desc", nativeQuery = true)
    List<Object> getFoodMostCommonByDates(@Param("datesToFilterBy") ArrayList<LocalDate> datesToFilterBy,  @Param("idToFilterBy") Integer idToFilterBy);

    @Query(value = "select food_id from food_log where user_id = :idToFilterBy group by food_id order by count(food_id) desc", nativeQuery = true)
    List<Object> getFoodMostCommonByUserId(@Param("idToFilterBy") Integer idToFilterBy);

    @Query(value = "select count(*) as count from food_log where food_id = :foodId", nativeQuery = true)
    int getFoodLogCount(@Param("foodId") Integer foodId);

    @Query(value = "select count(*) as count from food_log where food_id = :foodId and date_occured in (:datesToFilterBy)", nativeQuery = true)
    int getFoodLogCountByDates(@Param("foodId") Integer foodId, @Param("datesToFilterBy") ArrayList<LocalDate> datesToFilterBy);
}
