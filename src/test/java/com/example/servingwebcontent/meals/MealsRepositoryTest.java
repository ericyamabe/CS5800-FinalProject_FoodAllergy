package com.foodallergy.app.meals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MealsRepositoryMockTest {

    @Mock
    private MealsRepository mealsRepository;

    @Test
    void testFindByUserId() {
        Meals m1 = new Meals();
        m1.setId(1);
        m1.setUserId(10);
        m1.setName("Breakfast");

        Meals m2 = new Meals();
        m2.setId(2);
        m2.setUserId(10);
        m2.setName("Lunch");

        when(mealsRepository.findByUserId(10)).thenReturn(List.of(m1, m2));

        List<Meals> results = mealsRepository.findByUserId(10);

        assertEquals(2, results.size());
        assertEquals("Breakfast", results.get(0).getName());
        assertEquals("Lunch", results.get(1).getName());

        verify(mealsRepository).findByUserId(10);
    }

    @Test
    void testFindById() {
        Meals meal = new Meals();
        meal.setId(5);
        meal.setName("Dinner");

        when(mealsRepository.findById(5)).thenReturn(meal);

        Meals result = mealsRepository.findById(5);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Dinner", result.getName());

        verify(mealsRepository).findById(5);
    }
}