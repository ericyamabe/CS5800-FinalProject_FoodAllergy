package com.foodallergy.app.events;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventsFoodHashRepository extends CrudRepository<EventsFoodHash, Integer> {
    List<EventsFoodHash> findByFoodId(Integer FoodId);
    List<EventsFoodHash> findByEventId(Integer eventId);
}
