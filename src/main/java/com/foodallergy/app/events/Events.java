package com.foodallergy.app.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Events {
    public static Events instance;

    private Events() {}

    public static Events getInstance() {
        if (instance == null) {
            instance = new Events();
        }
        return instance;
    }

    public EventsEntity findEventById(EventsRepository eventsRepository, int eventId) {
        return eventsRepository.findById(eventId);
    }

    public List<EventLog> findEventLogByEventId(EventLogRepository eventLogRepository, int eventId) {
        return eventLogRepository.findByEventId(eventId);
    }

    public ArrayList<LocalDate> findEventLogDatesByEventId(EventLogRepository eventLogRepository, int eventId) {
        List<EventLog> eventLogs = eventLogRepository.findByEventId(eventId);
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

        for (EventLog eventLog : eventLogs) {
            dates.add(LocalDate.parse(eventLog.getDateOccured().toString()));
        }

        return dates;
    }

    public List<EventsEntity> getEventsIdsByCountByUserId(
            EventLogRepository eventLogRepository,
            EventsRepository eventsRepository,
            int userId) {
        List<Object> eventsLog = eventLogRepository.getEventsIdsByCountByUserId(userId);
        ArrayList<EventsEntity> events = new ArrayList<EventsEntity>();

        for (Object eventLog : eventsLog) {
            EventsEntity event = eventsRepository.findById(Integer.parseInt(eventLog.toString()));
            events.add(event);
        }

        return events;
    }
}
