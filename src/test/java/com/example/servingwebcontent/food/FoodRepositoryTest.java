package com.foodallergy.app.food;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FoodRepositoryTest {

    @Mock
    private FoodRepository foodRepository;

    @Test
    void testFindById() {
        Food f = new Food();
        f.setId(10);
        f.setName("Apple");

        when(foodRepository.findById(10)).thenReturn(f);

        Food result = foodRepository.findById(10);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Apple", result.getName());
        verify(foodRepository).findById(10);
    }

    @Test
    void testFindByUserId() {
        Food f1 = new Food();
        f1.setId(1);
        f1.setUserId(42);
        f1.setName("Bread");

        Food f2 = new Food();
        f2.setId(2);
        f2.setUserId(42);
        f2.setName("Cheese");

        when(foodRepository.findByUserId(42)).thenReturn(List.of(f1, f2));

        List<Food> results = foodRepository.findByUserId(42);

        assertEquals(2, results.size());
        assertEquals("Bread", results.get(0).getName());
        assertEquals("Cheese", results.get(1).getName());
        verify(foodRepository).findByUserId(42);
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        when(foodRepository.findById(999)).thenReturn(null);
        assertNull(foodRepository.findById(999));
    }
}