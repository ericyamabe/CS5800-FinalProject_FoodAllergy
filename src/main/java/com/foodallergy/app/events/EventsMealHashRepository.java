package com.foodallergy.app.events;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventsMealHashRepository extends CrudRepository<EventsMealHash, Integer> {
    List<EventsMealHash> findByMealId(Integer mealId);
    List<EventsMealHash> findByEventId(Integer eventId);
}
