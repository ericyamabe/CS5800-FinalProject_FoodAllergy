package com.foodallergy.app.meals;

import com.foodallergy.app.food.Food;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealsControllerTest {

    @Mock
    HttpSession session;

    @Mock
    MealsRepository mealsRepository;

    @Mock
    FoodRepository foodRepository;

    @Mock
    MealFoodHashRepository mealFoodHashRepository;

    @InjectMocks
    MealsController mealsController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

    // -------------------------------------------------------
    // GET /meals
    // -------------------------------------------------------

    @Test
    void meals_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String view = mealsController.meals(model);

        assertEquals("redirect:/login", view);
        verify(session).getAttribute("username");
    }

    @Test
    void meals_whenLoggedIn_populatesModelAndReturnsMealsView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(42);

        List<Meals> meals = new ArrayList<>();
        Meals m1 = new Meals();
        m1.setId(1);
        m1.setUserId(42);
        m1.setName("Breakfast");
        meals.add(m1);

        when(mealsRepository.findByUserId(42)).thenReturn(meals);

        String view = mealsController.meals(model);

        assertEquals("meals", view);
        assertEquals("Meals Home", model.getAttribute("pageTitle"));
        assertSame(meals, model.getAttribute("meals"));

        verify(mealsRepository).findByUserId(42);
    }

    // -------------------------------------------------------
    // GET /meals/add
    // -------------------------------------------------------

    @Test
    void logMeal_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String view = mealsController.logMeal(model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void logMeal_whenLoggedIn_setsPageTitleAndReturnsAddMealView() {
        when(session.getAttribute("username")).thenReturn("john");

        String view = mealsController.logMeal(model);

        assertEquals("addMeal", view);
        assertEquals("Add Meal", model.getAttribute("pageTitle"));
    }

    // -------------------------------------------------------
    // POST /meals/doAdd
    // -------------------------------------------------------

    @Test
    void doLogMeal_savesMealAndRedirectsToMealDetails() {
        when(session.getAttribute("userId")).thenReturn(77);

        // Make save() assign an ID to the meal
        when(mealsRepository.save(any(Meals.class))).thenAnswer(invocation -> {
            Meals meal = invocation.getArgument(0);
            meal.setId(123);
            return meal;
        });

        String view = mealsController.doLogMeal("My Meal");

        // verify redirect
        assertEquals("redirect:/meals/123", view);

        // capture saved meal
        ArgumentCaptor<Meals> captor = ArgumentCaptor.forClass(Meals.class);
        verify(mealsRepository).save(captor.capture());
        Meals saved = captor.getValue();

        assertEquals(77, saved.getUserId());
        assertEquals("My Meal", saved.getName());
    }

    // -------------------------------------------------------
    // GET /meals/{mealId}
    // -------------------------------------------------------

    @Test
    void mealsDetails_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String view = mealsController.meals(model, 5);

        assertEquals("redirect:/login", view);
    }

    @Test
    void mealsDetails_whenLoggedIn_populatesModelAndReturnsMealDetailsView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(99);

        int mealId = 10;

        Meals meal = new Meals();
        meal.setId(mealId);
        meal.setUserId(99);
        meal.setName("Lunch");
        when(mealsRepository.findById(mealId)).thenReturn(meal);

        // user foods
        Food userFood1 = new Food();
        userFood1.setId(1);
        userFood1.setUserId(99);
        userFood1.setName("Apple");

        Food userFood2 = new Food();
        userFood2.setId(2);
        userFood2.setUserId(99);
        userFood2.setName("Bread");

        when(foodRepository.findByUserId(99)).thenReturn(List.of(userFood1, userFood2));

        // associated foods via hashes
        MealFoodHash row1 = new MealFoodHash();
        row1.setMealId(mealId);
        row1.setFoodId(1);

        MealFoodHash row2 = new MealFoodHash();
        row2.setMealId(mealId);
        row2.setFoodId(2);

        when(mealFoodHashRepository.findByMealId(mealId)).thenReturn(List.of(row1, row2));
        when(foodRepository.findById(1)).thenReturn(userFood1);
        when(foodRepository.findById(2)).thenReturn(userFood2);

        String view = mealsController.meals(model, mealId);

        assertEquals("meal-details", view);
        assertEquals("Associate Foods", model.getAttribute("pageTitle"));
        assertEquals(mealId, model.getAttribute("mealId"));
        assertEquals("Lunch", model.getAttribute("mealName"));

        // foods available to user
        Object foodsAttr = model.getAttribute("foods");
        assertNotNull(foodsAttr);
        assertTrue(foodsAttr instanceof List<?>);
        List<?> foodsList = (List<?>) foodsAttr;
        assertEquals(2, foodsList.size());

        // associated foods
        Object assocAttr = model.getAttribute("associatedFoods");
        assertNotNull(assocAttr);
        assertTrue(assocAttr instanceof ArrayList<?>);
        ArrayList<?> assocList = (ArrayList<?>) assocAttr;
        assertEquals(2, assocList.size());
        assertSame(userFood1, assocList.get(0));
        assertSame(userFood2, assocList.get(1));

        verify(mealsRepository).findById(mealId);
        verify(mealFoodHashRepository).findByMealId(mealId);
        verify(foodRepository).findByUserId(99);
        verify(foodRepository).findById(1);
        verify(foodRepository).findById(2);
    }

    // -------------------------------------------------------
    // POST /meals/doAssociateFood
    // -------------------------------------------------------

    @Test
    void doAssociateFood_createsHashesForAllFoodIdsAndRedirects() {
        int mealId = 20;
        String foodIds = "1,2,3";

        String view = mealsController.doAssociateFood(mealId, foodIds);

        assertEquals("redirect:/meals/" + mealId, view);

        // verify 3 saves
        ArgumentCaptor<MealFoodHash> captor = ArgumentCaptor.forClass(MealFoodHash.class);
        verify(mealFoodHashRepository, times(3)).save(captor.capture());

        List<MealFoodHash> savedHashes = captor.getAllValues();
        assertEquals(3, savedHashes.size());

        assertEquals(mealId, savedHashes.get(0).getMealId());
        assertEquals(1, savedHashes.get(0).getFoodId());

        assertEquals(mealId, savedHashes.get(1).getMealId());
        assertEquals(2, savedHashes.get(1).getFoodId());

        assertEquals(mealId, savedHashes.get(2).getMealId());
        assertEquals(3, savedHashes.get(2).getFoodId());
    }
}