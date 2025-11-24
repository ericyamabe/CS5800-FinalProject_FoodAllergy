package com.foodallergy.app.events;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventsRepository extends CrudRepository<EventsEntity, Integer> {
    List<EventsEntity> findByUserId(int userId);
    EventsEntity findById(int eventId);
}
