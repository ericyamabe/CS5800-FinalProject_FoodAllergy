package com.foodallergy.app.meals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MealFoodHashRepositoryMockTest {

    @Mock
    private MealFoodHashRepository repository;

    @Test
    void testFindByMealId() {
        MealFoodHash h1 = new MealFoodHash();
        h1.setId(1);
        h1.setMealId(10);
        h1.setFoodId(100);

        MealFoodHash h2 = new MealFoodHash();
        h2.setId(2);
        h2.setMealId(10);
        h2.setFoodId(200);

        when(repository.findByMealId(10)).thenReturn(List.of(h1, h2));

        List<MealFoodHash> results = repository.findByMealId(10);

        assertEquals(2, results.size());
        assertEquals(10, results.get(0).getMealId());
        assertEquals(100, results.get(0).getFoodId());
        assertEquals(10, results.get(1).getMealId());
        assertEquals(200, results.get(1).getFoodId());

        verify(repository).findByMealId(10);
    }
}