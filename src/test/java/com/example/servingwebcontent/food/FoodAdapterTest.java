package com.example.servingwebcontent.food;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodAdapterTest {

    @Test
    void constructorWrapsFoodObject() {
        Food food = new Food();
        food.setId(10);
        food.setName("Apple");
        food.setUserId(5);

        FoodAdapter adapter = new FoodAdapter(food);

        assertEquals(10, adapter.getId());
        assertEquals("Apple", adapter.getName());
        assertEquals(5, adapter.getUserId());
    }
}

}