package com.foodallergy.app.registration;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StateIteratorTest {
    private static class TestStates extends States {
        private final ArrayList<String> states;

        TestStates(List<String> states) {
            this.states = new ArrayList<>(states);
        }

        @Override
        public ArrayList<String> getStates() {
            return states;
        }
    }

    @Test
    void iteratorIteratesThroughAllStatesInOrder() {
        List<String> sampleStates = List.of("California", "Texas", "New York");
        TestStates states = new TestStates(sampleStates);

        StateIterator iterator = new StateIterator(states);

        assertTrue(iterator.hasNext());
        assertEquals("California", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("Texas", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("New York", iterator.next());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    void hasNextReturnsFalseWhenEmpty() {
        TestStates states = new TestStates(List.of());
        StateIterator iterator = new StateIterator(states);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    void removeThrowsUnsupportedOperation() {
        TestStates states = new TestStates(List.of("California"));
        StateIterator iterator = new StateIterator(states);

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void nextReturnsNullWhenNoMoreElements() {
        TestStates states = new TestStates(List.of("California"));
        StateIterator iterator = new StateIterator(states);

        assertEquals("California", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());  // expected based on your implementation
    }
}
