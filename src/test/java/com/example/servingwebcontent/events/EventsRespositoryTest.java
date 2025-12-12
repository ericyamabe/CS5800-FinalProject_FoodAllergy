package com.foodallergy.app.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventsRepositoryUsageTest {

    @Mock
    private EventsRepository eventsRepository;

    @Test
    void testFindByUserId() {
        int userId = 10;

        EventsEntity event1 = new EventsEntity();
        event1.setId(1);
        event1.setUserId(userId);

        EventsEntity event2 = new EventsEntity();
        event2.setId(2);
        event2.setUserId(userId);

        when(eventsRepository.findByUserId(userId)).thenReturn(List.of(event1, event2));

        List<EventsEntity> result = eventsRepository.findByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        verify(eventsRepository).findByUserId(userId);
    }

    @Test
    void testFindById() {
        int eventId = 5;

        EventsEntity event = new EventsEntity();
        event.setId(eventId);

        when(eventsRepository.findById(eventId)).thenReturn(event);

        EventsEntity result = eventsRepository.findById(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());

        verify(eventsRepository).findById(eventId);
    }
}
