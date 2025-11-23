package com.foodallergy.app.events;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventLogRepository extends CrudRepository<EventLog, String> {
    List<EventLog> findByEventId(int eventId);

    @Query(value = "select event_id from event_log el where el.user_id = :idToFilterBy group by el.event_id order by count(*) desc", nativeQuery = true)
    List<Object> getEventsWithCountByUserId(@Param("idToFilterBy") int userId);
}