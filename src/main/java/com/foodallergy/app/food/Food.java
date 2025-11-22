package com.foodallergy.app.food;

import jakarta.persistence.*;

@Entity
@Table(name="food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int foodId;
    private String name;
    private int userId;
}
