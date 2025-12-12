package com.foodallergy.app.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsFoodHashRepositoryMockTest {

    @Mock
    private EventsFoodHashRepository repository;

    @Test
    void testFindByFoodId() {
        EventsFoodHash f1 = new EventsFoodHash();
        f1.setFoodId(10);
        f1.setEventId(1);

        EventsFoodHash f2 = new EventsFoodHash();
        f2.setFoodId(10);
        f2.setEventId(2);

        when(repository.findByFoodId(10)).thenReturn(List.of(f1, f2));

        List<EventsFoodHash> result = repository.findByFoodId(10);

        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getFoodId());
        assertEquals(10, result.get(1).getFoodId());

        verify(repository).findByFoodId(10);
    }

    @Test
    void testFindByEventId() {
        EventsFoodHash f1 = new EventsFoodHash();
        f1.setEventId(5);
        f1.setFoodId(100);

        when(repository.findByEventId(5)).thenReturn(List.of(f1));

        List<EventsFoodHash> result = repository.findByEventId(5);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getEventId());

        verify(repository).findByEventId(5);
    }
}
