package com.foodallergy.app.events;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface EventLogRepository extends CrudRepository<EventLog, String> {
    List<EventLog> findByEventId(int eventId);

    @Query(value = "select event_id from event_log where user_id = :idToFilterBy group by event_id order by count(*) desc", nativeQuery = true)
    List<Object> getEventsIdsByCountByUserId(@Param("idToFilterBy") int userId);

    @Query(value = "select event_id from event_log where date_occured in (:datesToFilterBy) and user_id = :idToFilterBy group by event_id order by count(*) desc", nativeQuery = true)
    List<Object> getEventsByDates(@Param("datesToFilterBy") ArrayList<LocalDate> datesToFilterBy,  @Param("idToFilterBy") Integer idToFilterBy);
}