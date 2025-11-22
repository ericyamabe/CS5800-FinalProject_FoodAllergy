package com.foodallergy.app.events;

import jakarta.persistence.*;

@Entity
@Table(name="events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;
    private String name;
    private int userId;
    private int mealId;
    private int foodId;
}
