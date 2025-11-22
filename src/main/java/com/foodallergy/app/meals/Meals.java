package com.foodallergy.app.meals;

import jakarta.persistence.*;

@Entity
@Table(name="meals")
public class Meals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mealId;
    private String name;
    private int userId;
}
