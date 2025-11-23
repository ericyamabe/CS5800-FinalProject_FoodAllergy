package com.foodallergy.app.meals;

import jakarta.persistence.*;

@Entity
@Table(name="meals")
public class Meals {
    @Id
    @Column(name="mealId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mealId;

    @Column(name="name")
    private String name;

    @Column(name="user_id")
    private int userId;

    public int getId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
