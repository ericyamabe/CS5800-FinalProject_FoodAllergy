package com.example.servingwebcontent.food;

import com.foodallergy.app.events.EventLogRepository;
import com.foodallergy.app.events.EventsEntity;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.events.FoodLog;
import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodController;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodControllerTest {

    @Mock
    HttpSession session;

    @Mock
    FoodRepository foodRepository;

    @Mock
    FoodLogRepository foodLogRepository;

    @Mock
    EventLogRepository eventLogRepository;

    @Mock
    EventsRepository eventsRepository;

    @InjectMocks
    FoodController foodController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

    // -------------------------------------------------------
    // GET /food
    // -------------------------------------------------------

    @Test
    void food_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String view = foodController.food(model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void food_whenLoggedIn_populatesModelAndReturnsFoodView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(42);

        Food f1 = new Food();
        f1.setId(1);
        f1.setUserId(42);
        f1.setName("Apple");

        Food f2 = new Food();
        f2.setId(2);
        f2.setUserId(42);
        f2.setName("Bread");

        when(foodRepository.findByUserId(42)).thenReturn(List.of(f1, f2));

        String view = foodController.food(model);

        assertEquals("food", view);
        assertEquals("Add Food", model.getAttribute("pageTitle"));

        Object foodsAttr = model.getAttribute("foods");
        assertNotNull(foodsAttr);
        assertTrue(foodsAttr instanceof List<?>);
        List<?> foods = (List<?>) foodsAttr;
        assertEquals(2, foods.size());

        verify(foodRepository).findByUserId(42);
    }

    // -------------------------------------------------------
    // GET /food/details/{foodId}
    // -------------------------------------------------------

    @Test
    void foodDetails_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String view = foodController.foodDetails(10, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void foodDetails_whenLoggedIn_populatesModelAndReturnsDetailsView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(99);

        int foodId = 5;

        Food food = new Food();
        food.setId(foodId);
        food.setUserId(99);
        food.setName("Milk");

        when(foodRepository.findById(foodId)).thenReturn(food);

        FoodLog log1 = new FoodLog();
        log1.setFoodId(foodId);
        log1.setUserId(99);
        log1.setDateOccured("2023-01-01");

        FoodLog log2 = new FoodLog();
        log2.setFoodId(foodId);
        log2.setUserId(99);
        log2.setDateOccured("2023-01-02");

        List<FoodLog> logs = List.of(log1, log2);
        when(foodLogRepository.findByFoodId(foodId)).thenReturn(logs);

        // event IDs returned by EventLogRepository
        List<Object> eventIds = List.of(10, 20);
        when(eventLogRepository.getEventsByDates(any(ArrayList.class), eq(99))).thenReturn(eventIds);

        EventsEntity e1 = new EventsEntity();
        e1.setId(10);
        e1.setName("Headache");

        EventsEntity e2 = new EventsEntity();
        e2.setId(20);
        e2.setName("Hives");

        when(eventsRepository.findById(10)).thenReturn(e1);
        when(eventsRepository.findById(20)).thenReturn(e2);

        String view = foodController.foodDetails(foodId, model);

        assertEquals("food-details", view);
        assertEquals("Details: Milk", model.getAttribute("pageTitle"));
        assertEquals("Milk", model.getAttribute("foodName"));
        assertSame(logs, model.getAttribute("foodLog"));

        Object eventsAttr = model.getAttribute("events");
        assertNotNull(eventsAttr);
        assertTrue(eventsAttr instanceof ArrayList<?>);
        ArrayList<?> events = (ArrayList<?>) eventsAttr;
        assertEquals(2, events.size());
        assertSame(e1, events.get(0));
        assertSame(e2, events.get(1));

        verify(foodRepository).findById(foodId);
        verify(foodLogRepository).findByFoodId(foodId);
        verify(eventLogRepository).getEventsByDates(any(ArrayList.class), eq(99));
        verify(eventsRepository).findById(10);
        verify(eventsRepository).findById(20);
    }

    // -------------------------------------------------------
    // GET /food/add
    // -------------------------------------------------------

    @Test
    void logFood_populatesModelAndReturnsAddFoodView() {
        when(session.getAttribute("userId")).thenReturn(42);

        Food f1 = new Food();
        f1.setId(1);
        f1.setUserId(42);
        f1.setName("Apple");

        when(foodRepository.findByUserId(42)).thenReturn(List.of(f1));

        String view = foodController.logFood(model);

        assertEquals("addFood.html", view);
        assertEquals("Add Food", model.getAttribute("pageTitle"));
        assertNotNull(model.getAttribute("foods"));
    }

    // -------------------------------------------------------
    // POST /food/doAddFood
    // -------------------------------------------------------

    @Test
    void doAddFood_withExistingId_onlyLogsFoodAndRedirects() {
        when(session.getAttribute("userId")).thenReturn(42);

        int existingId = 10;
        String date = "2023-02-01";

        String view = foodController.doAddFood(existingId, "", date);

        assertEquals("redirect:/food/details/" + existingId, view);

        // Should NOT create new Food
        verify(foodRepository, never()).save(any(Food.class));

        ArgumentCaptor<FoodLog> captor = ArgumentCaptor.forClass(FoodLog.class);
        verify(foodLogRepository).save(captor.capture());
        FoodLog savedLog = captor.getValue();

        assertEquals(existingId, savedLog.getFoodId());
        assertEquals(42, savedLog.getUserId());
        assertEquals(date, savedLog.getDateOccured().toString());
    }

    @Test
    void doAddFood_withoutExistingId_createsFoodAndLogsIt_thenRedirects() {
        when(session.getAttribute("userId")).thenReturn(99);

        // Make save(Food) assign an id
        when(foodRepository.save(any(Food.class))).thenAnswer(invocation -> {
            Food f = invocation.getArgument(0);
            f.setId(123);
            return f;
        });

        String name = "Orange";
        String date = "2023-03-01";

        String view = foodController.doAddFood(0, name, date);

        assertEquals("redirect:/food/details/123", view);

        // Verify food was created
        ArgumentCaptor<Food> foodCaptor = ArgumentCaptor.forClass(Food.class);
        verify(foodRepository).save(foodCaptor.capture());
        Food savedFood = foodCaptor.getValue();

        assertEquals("Orange", savedFood.getName());
        assertEquals(99, savedFood.getUserId());

        // Verify food log was created
        ArgumentCaptor<FoodLog> logCaptor = ArgumentCaptor.forClass(FoodLog.class);
        verify(foodLogRepository).save(logCaptor.capture());
        FoodLog savedLog = logCaptor.getValue();

        assertEquals(123, savedLog.getFoodId());
        assertEquals(99, savedLog.getUserId());
        assertEquals(date, savedLog.getDateOccured().toString());
    }
}