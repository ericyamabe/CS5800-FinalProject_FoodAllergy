package com.example.servingwebcontent.food;

import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import com.foodallergy.app.food.FoodsHelper;
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
class FoodsHelperTest {

    @Mock
    private FoodLogRepository foodLogRepository;

    @Mock
    private FoodRepository foodRepository;

    private FoodsHelper foodsHelper;

    @BeforeEach
    void setUp() {
        // use the singleton, same as production code\
        foodsHelper = FoodsHelper.getInstance();
    }

    // ----------------------------------------------------\
    // Singleton behavior\
    // ----------------------------------------------------\

    @Test
    void getInstance_returnsSameInstance() {
        FoodsHelper instance1 = FoodsHelper.getInstance();
        FoodsHelper instance2 = FoodsHelper.getInstance();

        assertSame(instance1, instance2);
    }

    // ----------------------------------------------------\
    // getMostCommonByUserId\
    // ----------------------------------------------------\

    @Test
    void getMostCommonByUserId_returnsFoodsInOrderFromRepositoryIds() {
        int userId = 42;

        // foodLogRepository returns IDs (as Objects)\
        List<Object> foodIds = List.of(1, 2, 3);
        when(foodLogRepository.getFoodMostCommonByUserId(userId)).thenReturn(foodIds);

        Food f1 = new Food();
        f1.setId(1);
        f1.setName("Apple");

        Food f2 = new Food();
        f2.setId(2);
        f2.setName("Bread");

        Food f3 = new Food();
        f3.setId(3);
        f3.setName("Cheese");

        when(foodRepository.findById(1)).thenReturn(f1);
        when(foodRepository.findById(2)).thenReturn(f2);
        when(foodRepository.findById(3)).thenReturn(f3);

        ArrayList<Food> result = foodsHelper.getMostCommonByUserId(
                foodLogRepository,
                foodRepository,
                userId
        );

        assertEquals(3, result.size());
        assertSame(f1, result.get(0));
        assertSame(f2, result.get(1));
        assertSame(f3, result.get(2));

        verify(foodLogRepository).getFoodMostCommonByUserId(userId);
        verify(foodRepository).findById(1);
        verify(foodRepository).findById(2);
        verify(foodRepository).findById(3);
    }

    @Test
    void getMostCommonByUserId_whenNoFoodLogs_returnsEmptyList() {
        int userId = 99;
        when(foodLogRepository.getFoodMostCommonByUserId(userId)).thenReturn(List.of());

        ArrayList<Food> result = foodsHelper.getMostCommonByUserId(
                foodLogRepository,
                foodRepository,
                userId
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(foodLogRepository).getFoodMostCommonByUserId(userId);
        verifyNoInteractions(foodRepository);
    }

    // ----------------------------------------------------\
    // getFoodMostCommonByDates\
    // ----------------------------------------------------\

    @Test
    void getFoodMostCommonByDates_returnsFoodsFromIdsInOrder() {
        int userId = 7;
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(2023, 1, 1));
        dates.add(LocalDate.of(2023, 1, 2));

        List<Object> ids = List.of(10, 20);
        when(foodLogRepository.getFoodMostCommonByDates(dates, userId)).thenReturn(ids);

        Food f1 = new Food();
        f1.setId(10);
        f1.setName("Milk");

        Food f2 = new Food();
        f2.setId(20);
        f2.setName("Eggs");

        when(foodRepository.findById(10)).thenReturn(f1);
        when(foodRepository.findById(20)).thenReturn(f2);

        ArrayList<Food> result = foodsHelper.getFoodMostCommonByDates(
                foodLogRepository,
                foodRepository,
                dates,
                userId
        );

        assertEquals(2, result.size());
        assertSame(f1, result.get(0));
        assertSame(f2, result.get(1));

        verify(foodLogRepository).getFoodMostCommonByDates(dates, userId);
        verify(foodRepository).findById(10);
        verify(foodRepository).findById(20);
    }

    @Test
    void getFoodMostCommonByDates_whenNoResults_returnsEmptyList() {
        int userId = 7;
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(2023, 1, 1));

        when(foodLogRepository.getFoodMostCommonByDates(dates, userId)).thenReturn(List.of());

        ArrayList<Food> result = foodsHelper.getFoodMostCommonByDates(
                foodLogRepository,
                foodRepository,
                dates,
                userId
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(foodLogRepository).getFoodMostCommonByDates(dates, userId);
        verifyNoInteractions(foodRepository);
    }
}