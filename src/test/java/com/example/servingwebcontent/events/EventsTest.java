package com.foodallergy.app.events;

import com.foodallergy.app.food.FoodLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsTest {

    @Mock
    private EventsRepository eventsRepository;

    @Mock
    private EventLogRepository eventLogRepository;

    @Mock
    private FoodLogRepository foodLogRepository;

    private Events events;

    @BeforeEach
    void setUp() {
        // Use the singleton instance, which is how your app uses it
        events = Events.getInstance();
    }

    @Test
    void getInstance_returnsSameInstance() {
        Events e1 = Events.getInstance();
        Events e2 = Events.getInstance();

        assertSame(e1, e2);
    }

    @Test
    void findEventById_delegatesToRepository() {
        int eventId = 10;
        EventsEntity entity = new EventsEntity();
        entity.setId(eventId);
        entity.setName("Headache");

        when(eventsRepository.findById(eventId)).thenReturn(entity);

        EventsEntity result = events.findEventById(eventsRepository, eventId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals("Headache", result.getName());
        verify(eventsRepository).findById(eventId);
    }

    @Test
    void findEventLogByEventId_delegatesToRepository() {
        int eventId = 5;

        EventLog log1 = new EventLog();
        log1.setEventId(eventId);
        log1.setDateOccured("2023-01-01");

        EventLog log2 = new EventLog();
        log2.setEventId(eventId);
        log2.setDateOccured("2023-01-02");

        List<EventLog> logs = List.of(log1, log2);

        when(eventLogRepository.findByEventId(eventId)).thenReturn(logs);

        List<EventLog> result = events.findEventLogByEventId(eventLogRepository, eventId);

        assertEquals(2, result.size());
        assertSame(logs, result);
        verify(eventLogRepository).findByEventId(eventId);
    }

    @Test
    void findEventLogDatesByEventId_extractsDatesCorrectly() {
        int eventId = 7;

        EventLog log1 = new EventLog();
        log1.setEventId(eventId);
        log1.setDateOccured("2023-01-01");

        EventLog log2 = new EventLog();
        log2.setEventId(eventId);
        log2.setDateOccured("2023-01-02");

        List<EventLog> logs = List.of(log1, log2);
        when(eventLogRepository.findByEventId(eventId)).thenReturn(logs);

        ArrayList<LocalDate> dates = events.findEventLogDatesByEventId(eventLogRepository, eventId);

        assertEquals(2, dates.size());
        assertEquals(LocalDate.of(2023, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2023, 1, 2), dates.get(1));
        verify(eventLogRepository).findByEventId(eventId);
    }

    @Test
    void getEventsIdsByCountByUserId_fetchesEventsByIdsFromRepository() {
        int userId = 42;

        // eventLogRepository returns a list of ids (as Objects)
        List<Object> eventIds = List.of(1, 2);
        when(eventLogRepository.getEventsIdsByCountByUserId(userId)).thenReturn(eventIds);

        EventsEntity e1 = new EventsEntity();
        e1.setId(1);
        e1.setName("Headache");

        EventsEntity e2 = new EventsEntity();
        e2.setId(2);
        e2.setName("Hives");

        when(eventsRepository.findById(1)).thenReturn(e1);
        when(eventsRepository.findById(2)).thenReturn(e2);

        List<EventsEntity> result =
                events.getEventsIdsByCountByUserId(eventLogRepository, eventsRepository, userId);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Headache", result.get(0).getName());
        assertEquals(2, result.get(1).getId());
        assertEquals("Hives", result.get(1).getName());

        verify(eventLogRepository).getEventsIdsByCountByUserId(userId);
        verify(eventsRepository).findById(1);
        verify(eventsRepository).findById(2);
    }

    @Test
    void getFoodRelatedToAllEventsCount_returnsPercentage() {
        int foodId = 10;
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(2023, 1, 1));
        dates.add(LocalDate.of(2023, 1, 2));

        when(foodLogRepository.getFoodLogCountByDates(foodId, dates)).thenReturn(3);
        when(foodLogRepository.getFoodLogCount(foodId)).thenReturn(6);

        float probability = events.getFoodRelatedToAllEventsCount(foodLogRepository, foodId, dates);

        assertEquals(50.0f, probability, 0.0001f);

        verify(foodLogRepository).getFoodLogCountByDates(foodId, dates);
        verify(foodLogRepository).getFoodLogCount(foodId);
    }
}
