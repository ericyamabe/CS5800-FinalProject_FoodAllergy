package com.foodallergy.app.registration;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class StatesTest {

    @Test
    void statesContainsAll50States() {
        States states = new States();
        ArrayList<String> list = states.getStates();

        assertNotNull(list);
        assertEquals(50, list.size());

        assertEquals("Alabama", list.get(0));
        assertEquals("North Carolina", list.get(32));
        assertEquals("Wyoming", list.get(49));
    }

    @Test
    void iteratorReturnsStateIterator() {
        States states = new States();
        Iterator<String> iterator = states.iterator();

        assertNotNull(iterator);
        assertTrue(iterator instanceof StateIterator);
    }

    @Test
    void iteratorIteratesThroughAllStatesInOrder() {
        States states = new States();
        Iterator<String> it = states.iterator();

        ArrayList<String> collected = new ArrayList<>();

        while (it.hasNext()) {
            collected.add(it.next());
        }

        assertEquals(50, collected.size());
        assertEquals(states.getStates(), collected);
    }

    @Test
    void iteratorStopsAfterLastElement() {
        States states = new States();
        Iterator<String> it = states.iterator();

        for (int i = 0; i < 50; i++) {
            assertTrue(it.hasNext());
            assertNotNull(it.next());
        }

        assertFalse(it.hasNext());
        assertNull(it.next());
    }
}
