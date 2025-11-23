package com.foodallergy.app.events;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventsRepository extends CrudRepository<Events, Integer> {
    List<Events> findByUserId(int userId);
    Events findById(int eventId);
}
